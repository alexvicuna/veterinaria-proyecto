package com.veterinaria.mascotas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"nombreMasc", "raza", "edad", "especie", "idDueno", "nombreDueno" })
public class MascotaRequestDTO {
    //Este request DTO viaja desde el cliente al servidor
    // No tiene lógica de negocio

    Long idMascota;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(min = 1, max = 100)
    private String nombreMasc;

    @NotBlank(message = "La especie es obligatoria")
    @Size(min = 1, max = 100)
    private String especie;

    @NotBlank(message = "La raza es obligatoria")
    private String raza;

    @NotNull(message = "La edad es obligatoria")
    @Positive(message = "La edad debe ser un número positivo")
    private int edad;

    private Long idDueno;
}


