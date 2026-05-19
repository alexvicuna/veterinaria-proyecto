package com.veterinaria.boleta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletaDTO {
    private Long idBoleta;
    private LocalDate fecha; // * fecha
    private Double total;    // * total

    private List<DetalleBoletaDTO> detalles;
}