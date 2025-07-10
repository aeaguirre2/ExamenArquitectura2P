package com.examen2p.sistema.controller;

import com.examen2p.sistema.controller.dto.*;
import com.examen2p.sistema.exception.AlertaDiferenciaMontoException;
import com.examen2p.sistema.exception.TurnoNotFoundException;
import com.examen2p.sistema.service.GestionCajaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/turnos")
@RequiredArgsConstructor
public class CajaController {
    private final GestionCajaService gestionCajaService;

    @Operation(summary = "Abrir turno de caja", description = "Inicia un nuevo turno para un cajero en una caja específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno abierto correctamente", content = @Content(schema = @Schema(implementation = TurnoCajaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping("/abrir")
    public ResponseEntity<TurnoCajaDTO> abrirTurno(@Valid @RequestBody TurnoCajaAperturaRequest request) {
        TurnoCajaDTO result = gestionCajaService.abrirTurno(request);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Procesar transacción de turno", description = "Registra una transacción (depósito o retiro) en un turno abierto.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transacción procesada correctamente", content = @Content(schema = @Schema(implementation = TransaccionTurnoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Turno no encontrado", content = @Content)
    })
    @PostMapping("/transaccion")
    public ResponseEntity<TransaccionTurnoDTO> procesarTransaccion(@Valid @RequestBody TransaccionTurnoRequest request) {
        TransaccionTurnoDTO result = gestionCajaService.procesarTransaccion(request);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Cerrar turno de caja", description = "Finaliza un turno, registrando las denominaciones y el monto final. Genera alerta si hay diferencia de montos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Turno cerrado correctamente", content = @Content(schema = @Schema(implementation = TurnoCajaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Diferencia de montos detectada", content = @Content),
        @ApiResponse(responseCode = "404", description = "Turno no encontrado", content = @Content)
    })
    @PatchMapping("/cerrar/{codigoTurno}")
    public ResponseEntity<TurnoCajaDTO> cerrarTurno(
            @Parameter(description = "Código del turno a cerrar") @PathVariable String codigoTurno,
            @Valid @RequestBody CierreTurnoRequest request) {
        TurnoCajaDTO result = gestionCajaService.cerrarTurno(codigoTurno, request.getDenominacionesFinales(), request.getMontoFinal());
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler({TurnoNotFoundException.class})
    public ResponseEntity<String> handleNotFound(TurnoNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler({AlertaDiferenciaMontoException.class})
    public ResponseEntity<String> handleAlerta(AlertaDiferenciaMontoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // DTO para el cierre de turno
    public static class CierreTurnoRequest {
        @Valid
        private List<DenominacionDTO> denominacionesFinales;
        @Valid
        private BigDecimal montoFinal;
        public List<DenominacionDTO> getDenominacionesFinales() { return denominacionesFinales; }
        public void setDenominacionesFinales(List<DenominacionDTO> denominacionesFinales) { this.denominacionesFinales = denominacionesFinales; }
        public BigDecimal getMontoFinal() { return montoFinal; }
        public void setMontoFinal(BigDecimal montoFinal) { this.montoFinal = montoFinal; }
    }
} 