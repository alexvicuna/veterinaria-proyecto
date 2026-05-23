package com.veterinaria.boleta.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleBoletaRequestDTO {

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El subtotal es obligatorio")
    private Double subtotal;
}