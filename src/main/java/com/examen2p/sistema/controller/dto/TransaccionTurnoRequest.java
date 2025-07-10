package com.examen2p.sistema.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.*;
import com.examen2p.sistema.enums.TipoTransaccion;

@Data
@NoArgsConstructor
public class TransaccionTurnoRequest {
    @NotBlank(message = "El código de caja es requerido")
    private String codigoCaja;

    @NotBlank(message = "El código de cajero es requerido")
    private String codigoCajero;

    @NotBlank(message = "El código de turno es requerido")
    private String codigoTurno;

    @NotNull(message = "El tipo de transacción es requerido")
    private TipoTransaccion tipoTransaccion;

    @NotNull(message = "El monto total es requerido")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private BigDecimal montoTotal;

    @NotNull(message = "Las denominaciones son requeridas")
    @Size(min = 1, message = "Debe haber al menos una denominación")
    private List<DenominacionDTO> denominaciones;
} 