package com.veterinaria.boleta.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CitaDTO {
    private Long idCita;
    private LocalDateTime fechaCita;
    private String motivoConsulta;
    private String estadoCita;
}