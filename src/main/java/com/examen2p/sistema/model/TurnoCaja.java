package com.examen2p.sistema.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.examen2p.sistema.controller.dto.DenominacionDTO;
import com.examen2p.sistema.enums.EstadoTurno;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "turnos_caja")
public class TurnoCaja {
    @Id
    private String codigoTurno;
    private String codigoCaja;
    private String codigoCajero;
    private LocalDateTime inicioTurno;
    private BigDecimal montoInicial;
    private LocalDateTime finTurno;
    private BigDecimal montoFinal;
    private EstadoTurno estado;
    private List<DenominacionDTO> denominacionesIniciales;
    private List<DenominacionDTO> denominacionesFinales;

    public TurnoCaja(String codigoTurno) {
        this.codigoTurno = codigoTurno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnoCaja that = (TurnoCaja) o;
        return codigoTurno.equals(that.codigoTurno);
    }

    @Override
    public int hashCode() {
        return codigoTurno.hashCode();
    }
} 