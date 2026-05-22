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
    private Long idReceta;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private String diagnostico;

    @Column(nullable = false)
    private String medicamento;

    @Column(nullable = false)
    private String dosis;

    @Column(nullable = false)
    private Long idVeterinario;

    @Column(nullable = false)
    private Long idMascota;
}