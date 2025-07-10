package com.examen2p.sistema.exception;

public class TurnoNotFoundException extends RuntimeException {
    private final String codigoTurno;
    public TurnoNotFoundException(String codigoTurno) {
        super();
        this.codigoTurno = codigoTurno;
    }
    @Override
    public String getMessage() {
        return "No se encontró el turno con código: " + this.codigoTurno;
    }
} 