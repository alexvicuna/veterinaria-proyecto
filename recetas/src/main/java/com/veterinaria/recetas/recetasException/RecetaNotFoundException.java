package com.veterinaria.recetas.recetasException;

public class RecetaNotFoundException extends RuntimeException {
    public RecetaNotFoundException(Long id) {
        super("Receta no encontrada con el ID: " + id);
    }
}