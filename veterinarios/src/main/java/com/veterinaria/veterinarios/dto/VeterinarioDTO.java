package com.veterinaria.veterinarios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class VeterinarioDTO {
    private Long idVeterinario;

    @NotBlank(message = "El nombre del veterinario es obligatorio")
    private String nombreVet;

    private String especialidad;
    private String telefono;
}