package com.veterinaria.recetas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CitaDTO {
    private Long idCita;
    private String fechaCita;
    private String motivoConsulta;
    private String estadoCita;
}