package com.veterinaria.usuarios.UsuariosException;

public class UsuariosNotFoundException extends RuntimeException {

    public UsuariosNotFoundException(Long id) {
        super("Usuario no encontrado con ID: " + id);
    }

    public UsuariosNotFoundException(String message) {
        super(message);
    }
}
