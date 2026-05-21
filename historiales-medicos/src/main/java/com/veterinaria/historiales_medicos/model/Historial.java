package com.veterinaria.historiales_medicos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor

public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    private String diagnostico;
    private String tratamiento;
    private String vacunas;

    private Long idMascota;
}
