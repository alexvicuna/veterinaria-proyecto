package com.veterinaria.recetas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "receta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta")
    private Long idReceta;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "diagnostico", nullable = false)
    private String diagnostico;

    @Column(name = "medicamento", nullable = false)
    private String medicamento;

    @Column(name = "dosis", nullable = false)
    private String dosis;

    @Column(name = "id_veterinario", nullable = false)
    private Long idVeterinario;

    @Column(name = "id_mascota", nullable = false)
    private Long idMascota;

    @Column(name = "id_cita", nullable = false)
    private Long idCita;
}