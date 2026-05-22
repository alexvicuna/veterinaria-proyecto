package com.veterinaria.boleta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleBoletaResponseDTO {

    private Long idDetalle;
    private Integer cantidad;
    private Double subtotal;
    private Long idBoleta;
}