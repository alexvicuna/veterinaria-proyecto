package com.veterinaria.historiales_medicos.historialException;

public class HistorialNotFoundException extends RuntimeException {
    public HistorialNotFoundException(String message) {
        super(message);
    }
    public HistorialNotFoundException(Long id) {
        super("Historial no encontrado con id: " + id);
    }
}
