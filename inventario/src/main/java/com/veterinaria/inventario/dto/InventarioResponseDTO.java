// InventarioResponseDTO.java
package com.veterinaria.inventario.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventarioResponseDTO {

    private Long idProducto;
    private String nombreProducto;
    private String descripcion;
    private String categoriaProducto;
    private Integer cantidadDisponible;
}