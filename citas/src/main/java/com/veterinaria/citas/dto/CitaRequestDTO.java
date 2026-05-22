package com.veterinaria.citas.dto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.veterinaria.citas.model.EstadoCita;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"fechaCita", "motivoConsulta", "estadoCita", "idMascota", "idDueno", "idVeterinario"})
public class CitaRequestDTO {

    private Long idCita;

    @NotNull(message = "La fecha de la cita no puede ser nula")
    @Future(message = "La fecha de la cita debe ser en el futuro")
    private LocalDateTime fechaCita;  // ← Date → LocalDateTime

    @NotBlank(message = "El motivo de la cita no puede estar vacío")
    private String motivoConsulta;

    @NotNull(message = "El estado de la cita es obligatorio")
    private EstadoCita estadoCita;

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long idMascota;

    @NotNull(message = "El ID del dueño es obligatorio")
    private Long idDueno;

    @NotNull(message = "El ID del veterinario es obligatorio")
    private Long idVeterinario;
}