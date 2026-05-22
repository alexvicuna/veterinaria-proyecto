package com.veterinaria.recetas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VeterinarioDTO {
    private Long idVeterinario;
    private String nombreVet;
    private String apellidoVet;
    private String especialidad;
}