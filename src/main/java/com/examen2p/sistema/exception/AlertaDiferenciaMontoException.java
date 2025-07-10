package com.examen2p.sistema.exception;

public class AlertaDiferenciaMontoException extends RuntimeException {
    private final String codigoTurno;
    private final String mensaje;
    public AlertaDiferenciaMontoException(String codigoTurno, String mensaje) {
        super();
        this.codigoTurno = codigoTurno;
        this.mensaje = mensaje;
    }
    @Override
    public String getMessage() {
        return "Alerta de diferencia de monto en el turno " + codigoTurno + ": " + mensaje;
    }
} 