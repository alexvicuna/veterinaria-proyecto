package com.veterinaria.historiales_medicos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialDTO {

    private Long idHistorial;

    @NotBlank(message = "El diagnóstico médico es obligatorio")
    private String diagnostico;

    @NotBlank(message = "El tratamiento no puede estar vacío")
    private String tratamiento;

    private String vacunas;

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long idMascota;
}