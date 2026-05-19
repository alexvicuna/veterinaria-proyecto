package com.veterinaria.mascotas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class MascotaResponseDTO {

    private Long idMascota;
    private String nombreMasc;
    private String especie;
    private String raza;
    private int edad;

    private DuenoDTO dueno;
}

