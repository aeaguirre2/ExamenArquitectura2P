package com.examen2p.sistema.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import com.examen2p.sistema.controller.dto.TurnoCajaDTO;
import com.examen2p.sistema.model.TurnoCaja;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TurnoCajaMapper {
    TurnoCajaDTO toDTO(TurnoCaja model);
    TurnoCaja toModel(TurnoCajaDTO dto);
} 