package com.examen2p.sistema.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.examen2p.sistema.controller.dto.DenominacionDTO;
import com.examen2p.sistema.enums.TipoTransaccion;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "transacciones_turno")
public class TransaccionTurno {
    @Id
    private String id;
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;
    private TipoTransaccion tipoTransaccion;
    private BigDecimal montoTotal;
    private List<DenominacionDTO> denominaciones;
    private LocalDateTime fecha;

    public TransaccionTurno(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransaccionTurno that = (TransaccionTurno) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
} 