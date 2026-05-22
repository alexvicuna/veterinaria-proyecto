package com.veterinaria.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(nullable = false)
    private String nombreProducto;

    private String descripcion;

    @Column(nullable = false)
    private String categoriaProducto;

    @Column(nullable = false)
    private Integer cantidadDisponible;
}