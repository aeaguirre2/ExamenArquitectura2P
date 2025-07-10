package com.examen2p.sistema.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
public class DenominacionDTO {
    @NotNull(message = "El valor de la denominación es requerido")
    @DecimalMin(value = "1.0", message = "Valor mínimo es 1")
    private BigDecimal valor;

    @NotNull(message = "La cantidad es requerida")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotNull(message = "El monto es requerido")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private BigDecimal monto;
} 