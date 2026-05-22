package com.veterinaria.recetas.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MascotaDTO {
    private Long idMascota;
    private String nombreMasc;
    private String especie;
    private String raza;
    private int edad;

}
