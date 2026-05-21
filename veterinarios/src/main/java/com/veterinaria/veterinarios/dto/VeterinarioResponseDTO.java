package com.veterinaria.veterinarios.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class VeterinarioResponseDTO {
    private Long idVeterinario;
    private String rutVet;
    private String nombreVet;
    private String apellidoVet;
    private String especialidad;
    private String telefono;
    private String correo;
}