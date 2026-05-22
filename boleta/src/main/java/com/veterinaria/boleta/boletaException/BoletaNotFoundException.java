package com.veterinaria.boleta.boletaException;

public class BoletaNotFoundException extends RuntimeException {

    public BoletaNotFoundException(Long id) {
        super("Boleta no encontrada con ID: " + id);
    }

    public BoletaNotFoundException(String message) {
        super(message);
    }
}
