package com.veterinaria.recetas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"idReceta", "fechaEmision", "diagnostico", "medicamento", "dosis", "cita", "mascota", "veterinario"})
public class RecetaResponseDTO {

    private Long idReceta;
    private LocalDate fechaEmision;
    private String diagnostico;
    private String medicamento;
    private String dosis;
    private MascotaDTO mascota;
    private VeterinarioDTO veterinario;
    private CitaDTO cita;
}