package com.veterinaria.boleta.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "boletas")
@Getter
@Setter
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boleta")
    private Long idBoleta;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "id_cita", nullable = false)
    private Long idCita;

    @Column(name = "id_pago", nullable = false)
    private Long idPago;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleBoleta> detalles;
}