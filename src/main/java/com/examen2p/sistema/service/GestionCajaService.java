package com.examen2p.sistema.service;

import com.examen2p.sistema.controller.dto.*;
import com.examen2p.sistema.controller.mapper.TurnoCajaMapper;
import com.examen2p.sistema.controller.mapper.TransaccionTurnoMapper;
import com.examen2p.sistema.enums.EstadoTurno;
import com.examen2p.sistema.enums.TipoTransaccion;
import com.examen2p.sistema.exception.AlertaDiferenciaMontoException;
import com.examen2p.sistema.exception.TurnoNotFoundException;
import com.examen2p.sistema.model.TurnoCaja;
import com.examen2p.sistema.model.TransaccionTurno;
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
    public TurnoCajaDTO abrirTurno(TurnoCajaDTO dto) {
        String codigoTurno = generarCodigoTurno(dto.getCodigoCaja(), dto.getCodigoCajero(), LocalDateTime.now());
        dto.setCodigoTurno(codigoTurno);
        dto.setInicioTurno(LocalDateTime.now());
        dto.setEstado(EstadoTurno.ABIERTO);
        TurnoCaja turno = turnoCajaMapper.toModel(dto);
        turnoCajaRepository.save(turno);
        log.info("Turno abierto: {}", codigoTurno);
        return turnoCajaMapper.toDTO(turno);
    }

    @Transactional
    public TransaccionTurnoDTO procesarTransaccion(TransaccionTurnoDTO dto) {
        TurnoCaja turno = turnoCajaRepository.findById(dto.getCodigoTurno())
                .orElseThrow(() -> new TurnoNotFoundException(dto.getCodigoTurno()));
        if (turno.getEstado() != EstadoTurno.ABIERTO) {
            throw new RuntimeException("El turno no está abierto");
        }
        dto.setFecha(LocalDateTime.now());
        TransaccionTurno transaccion = transaccionTurnoMapper.toModel(dto);
        transaccionTurnoRepository.save(transaccion);
        log.info("Transacción procesada para turno: {}", dto.getCodigoTurno());
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

    // Repositorios internos para MongoDB
    public interface TurnoCajaRepository extends MongoRepository<TurnoCaja, String> {}
    public interface TransaccionTurnoRepository extends MongoRepository<TransaccionTurno, String> {
        List<TransaccionTurno> findByCodigoTurno(String codigoTurno);
    }
} 