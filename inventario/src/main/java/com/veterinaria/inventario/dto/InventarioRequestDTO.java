<<<<<<< HEAD
package com.veterinaria.inventario.dto;

public class InventarioRequestDTO {
}
=======
// InventarioRequestDTO.java
package com.veterinaria.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventarioRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombreProducto;

    private String descripcion; // opcional

    @NotBlank(message = "La categoría del producto es obligatoria")
    private String categoriaProducto;

    @NotNull(message = "La cantidad disponible es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidadDisponible;
}
>>>>>>> 0429cfed3641891bf219397071c83ecf49cf9344
