package com.veterinaria.veterinarios.veterinariosException;

public class VeterinariosNotFoundException extends RuntimeException {

        public VeterinariosNotFoundException(Long id) {
            super("No se encontró un dueño con el ID: " + id);
        }
        public VeterinariosNotFoundException(String rut) {
            super("No se encontró un dueño con el RUT: " + rut);
        }
}

