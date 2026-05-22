package com.veterinaria.inventario.inventarioException;

public class InventarioNotFoundException extends RuntimeException {
    public InventarioNotFoundException(Long id) {
        super("Producto no encontrado con el ID: " + id);
    }
}