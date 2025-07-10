package com.examen2p.sistema.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import com.examen2p.sistema.controller.dto.TransaccionTurnoDTO;
import com.examen2p.sistema.model.TransaccionTurno;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TransaccionTurnoMapper {
    TransaccionTurnoDTO toDTO(TransaccionTurno model);
    TransaccionTurno toModel(TransaccionTurnoDTO dto);
} 