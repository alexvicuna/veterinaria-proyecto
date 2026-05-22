package com.veterinaria.historiales_medicos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor

public class Historial {
    @Id


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    @Column(nullable = false)
    private String diagnostico;

    @Column(nullable = false)
    private String tratamiento;

    private String vacunas;

    @Column(nullable = false)
    private LocalDate fechaAtencion;

    private String observaciones;

    @Column(nullable = false)
    private Long idMascota;

    private Long idVeterinario;


}
