package com.veterinaria.veterinarios.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"nombreVet", "especialidad", "telefono"})
public class VeterinarioRequestDTO {

    @NotBlank(message = "El nombre del veterinario es obligatorio")
    @Size(min = 1, max = 150, message = "El nombre debe tener entre 1 y 150 caracteres")
    private String nombreVet;

    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    private String especialidad;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;
}