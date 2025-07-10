package com.examen2p.sistema.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
public class TurnoCajaAperturaRequest {
    @NotBlank(message = "El código de caja es requerido")
    private String codigoCaja;

    @NotBlank(message = "El código de cajero es requerido")
    private String codigoCajero;

    @NotNull(message = "El monto inicial es requerido")
    @DecimalMin(value = "0.0", message = "El monto inicial no puede ser negativo")
    private BigDecimal montoInicial;

    @NotNull(message = "Las denominaciones iniciales son requeridas")
    @Size(min = 1, message = "Debe haber al menos una denominación")
    private List<DenominacionDTO> denominacionesIniciales;
} 