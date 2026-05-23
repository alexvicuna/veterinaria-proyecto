package com.veterinaria.boleta.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoletaRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El id de la cita es obligatorio")
    private Long idCita;

    @NotNull(message = "El id del pago es obligatorio")
    private Long idPago;

    @Valid
    @NotNull(message = "Los detalles son obligatorios")
    private List<DetalleBoletaRequestDTO> detalles;
}