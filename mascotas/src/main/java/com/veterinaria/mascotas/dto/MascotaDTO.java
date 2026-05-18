package com.veterinaria.mascotas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "idMascota", "nombreMasc", "raza", "edad", "especie", "idDueno", "nombreDueno" })
public class MascotaDTO {

    private Long idMascota; // <-- Tu variable real

    @NotBlank(message = "El nombre de la mascota no puede estar vacío")
    private String nombreMasc;

    @NotBlank(message = "La especie es obligatoria")
    private String especie;

    @NotBlank(message = "La raza es obligatoria")
    private String raza;

    @NotNull(message = "La edad es obligatoria")
    private int edad;

    private Long idDueno;
    private Object nombreDueno;
}

