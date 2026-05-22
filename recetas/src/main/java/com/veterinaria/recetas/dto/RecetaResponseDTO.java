package com.veterinaria.recetas.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecetaResponseDTO {

    private Long idReceta;
    private LocalDate fechaEmision;
    private String diagnostico;
    private String medicamento;
    private String dosis;
    private Long idVeterinario;
    private Long idMascota;
}