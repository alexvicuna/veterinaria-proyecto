package com.veterinaria.citas.dto;

import com.veterinaria.citas.model.EstadoCita;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoDTO {

    @NotNull(message = "El estado no puede ser nulo")
    private EstadoCita estadoCita;
}
