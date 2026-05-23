package com.veterinaria.usuarios.dto;

import com.veterinaria.usuarios.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private Rol rol;
    private LocalDateTime fechaCreacion;
    private Boolean activo;
}