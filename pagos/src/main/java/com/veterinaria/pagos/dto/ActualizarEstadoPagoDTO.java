package com.veterinaria.pagos.dto;

import com.veterinaria.pagos.model.DetallePago;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoPagoDTO {

    @NotNull(message = "El estado no puede ser nulo")
    private DetallePago detallePago;
}