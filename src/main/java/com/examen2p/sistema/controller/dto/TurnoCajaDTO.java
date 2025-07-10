package com.examen2p.sistema.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.*;
import com.examen2p.sistema.enums.EstadoTurno;

@Data
@NoArgsConstructor
public class TurnoCajaDTO {
    private String codigoTurno;

    @NotBlank(message = "El código de caja es requerido")
    private String codigoCaja;

    @NotBlank(message = "El código de cajero es requerido")
    private String codigoCajero;

    private LocalDateTime inicioTurno;

    @NotNull(message = "El monto inicial es requerido")
    @DecimalMin(value = "0.0", message = "El monto inicial no puede ser negativo")
    private BigDecimal montoInicial;

    private LocalDateTime finTurno;

    private BigDecimal montoFinal;

    private EstadoTurno estado;

    @NotNull(message = "Las denominaciones iniciales son requeridas")
    private List<DenominacionDTO> denominacionesIniciales;

    private List<DenominacionDTO> denominacionesFinales;
} 