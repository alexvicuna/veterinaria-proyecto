package com.veterinaria.boleta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoletaResponseDTO {

    private Long idBoleta;
    private LocalDate fecha;
    private Double total;
    private List<DetalleBoletaResponseDTO> detalles;
}