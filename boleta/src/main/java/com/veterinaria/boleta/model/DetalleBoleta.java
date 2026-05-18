package com.veterinaria.boleta.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalles_boleta")
@Data
public class DetalleBoleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;
    private Integer cantidad;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "fk_boleta")
    private Boleta boleta;
}