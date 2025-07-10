package com.examen2p.sistema.service;

import com.examen2p.sistema.controller.dto.*;
import com.examen2p.sistema.controller.dto.TurnoCajaAperturaRequest;
import com.examen2p.sistema.controller.dto.TransaccionTurnoRequest;
import com.examen2p.sistema.controller.mapper.TurnoCajaMapper;
import com.examen2p.sistema.controller.mapper.TransaccionTurnoMapper;
import com.examen2p.sistema.enums.EstadoTurno;
import com.examen2p.sistema.enums.TipoTransaccion;
import com.examen2p.sistema.exception.AlertaDiferenciaMontoException;
import com.examen2p.sistema.exception.TurnoNotFoundException;
import com.examen2p.sistema.model.TurnoCaja;
import com.examen2p.sistema.model.TransaccionTurno;
import com.examen2p.sistema.repository.TurnoCajaRepository;
import com.examen2p.sistema.repository.TransaccionTurnoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GestionCajaService {
    private final TurnoCajaRepository turnoCajaRepository;
    private final TransaccionTurnoRepository transaccionTurnoRepository;
    private final TurnoCajaMapper turnoCajaMapper;
    private final TransaccionTurnoMapper transaccionTurnoMapper;

    @Transactional
    public TurnoCajaDTO abrirTurno(TurnoCajaAperturaRequest request) {
        // Mapear manualmente los campos necesarios
        TurnoCaja turno = new TurnoCaja();
        turno.setCodigoCaja(request.getCodigoCaja());
        turno.setCodigoCajero(request.getCodigoCajero());
        turno.setInicioTurno(java.time.LocalDateTime.now());
        turno.setMontoInicial(request.getMontoInicial());
        turno.setDenominacionesIniciales(request.getDenominacionesIniciales());
        turno.setEstado(com.examen2p.sistema.enums.EstadoTurno.ABIERTO);
        // Código de turno generado
        String codigoTurno = request.getCodigoCaja() + "-" + request.getCodigoCajero() + "-" + java.time.LocalDate.now().toString().replace("-", "");
        turno.setCodigoTurno(codigoTurno);
        // Guardar en la base de datos
        turnoCajaRepository.save(turno);
        return turnoCajaMapper.toDTO(turno);
    }

    @Transactional
    public TransaccionTurnoDTO procesarTransaccion(TransaccionTurnoRequest request) {
        // Mapear manualmente los campos necesarios
        TransaccionTurno transaccion = new TransaccionTurno();
        transaccion.setCodigoCaja(request.getCodigoCaja());
        transaccion.setCodigoCajero(request.getCodigoCajero());
        transaccion.setCodigoTurno(request.getCodigoTurno());
        transaccion.setTipoTransaccion(request.getTipoTransaccion());
        transaccion.setMontoTotal(request.getMontoTotal());
        transaccion.setDenominaciones(request.getDenominaciones());
        transaccion.setFecha(java.time.LocalDateTime.now());
        // Guardar en la base de datos
        transaccionTurnoRepository.save(transaccion);
        return transaccionTurnoMapper.toDTO(transaccion);
    }

    @Transactional
    public TurnoCajaDTO cerrarTurno(String codigoTurno, List<DenominacionDTO> denominacionesFinales, BigDecimal montoFinal) {
        TurnoCaja turno = turnoCajaRepository.findById(codigoTurno)
                .orElseThrow(() -> new TurnoNotFoundException(codigoTurno));
        if (turno.getEstado() != EstadoTurno.ABIERTO) {
            throw new RuntimeException("El turno ya está cerrado");
        }
        turno.setFinTurno(LocalDateTime.now());
        turno.setDenominacionesFinales(denominacionesFinales);
        turno.setMontoFinal(montoFinal);
        turno.setEstado(EstadoTurno.CERRADO);
        // Calcular el monto esperado según las transacciones
        BigDecimal montoCalculado = calcularMontoFinal(turno.getCodigoTurno());
        if (montoCalculado.compareTo(montoFinal) != 0) {
            log.warn("Diferencia de monto al cerrar turno {}: esperado={}, ingresado={}", codigoTurno, montoCalculado, montoFinal);
            throw new AlertaDiferenciaMontoException(codigoTurno, "Esperado: " + montoCalculado + ", Ingresado: " + montoFinal);
        }
        turnoCajaRepository.save(turno);
        log.info("Turno cerrado: {}", codigoTurno);
        return turnoCajaMapper.toDTO(turno);
    }

    private String generarCodigoTurno(String codigoCaja, String codigoCajero, LocalDateTime fecha) {
        return String.format("%s-%s-%04d%02d%02d", codigoCaja, codigoCajero, fecha.getYear(), fecha.getMonthValue(), fecha.getDayOfMonth());
    }

    private BigDecimal calcularMontoFinal(String codigoTurno) {
        List<TransaccionTurno> transacciones = transaccionTurnoRepository.findByCodigoTurno(codigoTurno);
        BigDecimal total = BigDecimal.ZERO;
        for (TransaccionTurno t : transacciones) {
            if (t.getTipoTransaccion() == TipoTransaccion.DEPOSITO) {
                total = total.add(t.getMontoTotal());
            } else if (t.getTipoTransaccion() == TipoTransaccion.RETIRO) {
                total = total.subtract(t.getMontoTotal());
            }
        }
        return total;
    }
} 