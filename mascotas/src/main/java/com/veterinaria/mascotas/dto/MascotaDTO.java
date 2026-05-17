package com.veterinaria.mascotas.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor

public class MascotaDTO {


    @NotBlank(message = "El nombre de la mascota no puede estar vacío")

    private String nombreMasc;

    @NotBlank(message = "La especie es obligatoria")
    private String especie;

    @NotBlank(message = "La raza es obligatoria")
    private String raza;

    @NotBlank(message = "La edad es obligatoria")
    private String edad;

}


