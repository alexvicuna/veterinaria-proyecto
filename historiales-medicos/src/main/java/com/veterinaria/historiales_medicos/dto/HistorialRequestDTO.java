package com.veterinaria.historiales_medicos.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialRequestDTO {

    @NotBlank(message = "El diagnóstico es obligatorio")
    private String diagnostico;

    @NotBlank(message = "El tratamiento es obligatorio")
    private String tratamiento;

    private String vacunas;        // opcional, no siempre hay vacunas

    private String observaciones;  // opcional, notas extra del veterinario

    @NotNull(message = "La fecha de atención es obligatoria")
    private LocalDate fechaAtencion;

    @NotNull(message = "El id de la mascota es obligatorio")
    private Long idMascota;

    @NotNull(message = "El id del veterinario es obligatorio")
    private Long idVeterinario;
}