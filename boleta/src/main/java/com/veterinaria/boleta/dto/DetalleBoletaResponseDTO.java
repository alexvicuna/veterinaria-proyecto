package com.veterinaria.boleta.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleBoletaResponseDTO {

    private Long idDetalle;
    private String descripcion;
    private Integer cantidad;
    private Double subtotal;
}