package com.veterinaria.usuarios.UsuariosException;

public class EmailYaExisteException extends RuntimeException {

    public EmailYaExisteException(String email) {
        super("Ya existe un usuario registrado con el email: " + email);
    }
}
