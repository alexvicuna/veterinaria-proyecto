package com.veterinaria.citas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.veterinaria.citas.model.EstadoCita;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonPropertyOrder({"idCita", "fechaCita", "motivoConsulta", "estadoCita", "mascota", "dueno", "veterinario"})
public class CitaResponseDTO {

    private Long idCita;
    private LocalDateTime fechaCita;
    private String motivoConsulta;
    private EstadoCita estadoCita;

    private MascotaDTO mascota;
    private DuenoDTO dueno;
    private VeterinarioDTO veterinario;

}
