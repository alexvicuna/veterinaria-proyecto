package com.veterinaria.mascotas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MascotaDTO {
    private Long idMascota;
    private String nombreMasc;
    private String especie;
    private String raza;
    private int edad;
}