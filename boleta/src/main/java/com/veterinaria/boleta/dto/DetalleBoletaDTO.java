package com.veterinaria.boleta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleBoletaDTO {
    private Long idDetalle;
    private Integer cantidad; // * cantidad
    private Double subtotal;  // * subtotal
}