package com.veterinaria.duenos.duenoException;

public class DuenoNotFoundException extends RuntimeException {
    public DuenoNotFoundException(Long id) {
        super("No se encontró un dueño con el ID: " + id);
    }
    public DuenoNotFoundException(String rut) {
        super("No se encontró un dueño con el RUT: " + rut);
    }

}
