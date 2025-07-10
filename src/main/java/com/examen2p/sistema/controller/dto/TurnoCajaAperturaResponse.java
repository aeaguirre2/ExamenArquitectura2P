package com.examen2p.sistema.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.examen2p.sistema.enums.EstadoTurno;

@Data
@NoArgsConstructor
public class TurnoCajaAperturaResponse {
    private String codigoTurno;
    private String codigoCaja;
    private String codigoCajero;
    private LocalDateTime inicioTurno;
    private BigDecimal montoInicial;
    private EstadoTurno estado;
    private List<DenominacionDTO> denominacionesIniciales;
} 