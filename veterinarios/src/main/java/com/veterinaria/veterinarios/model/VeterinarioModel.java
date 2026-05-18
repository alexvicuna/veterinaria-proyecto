package com.veterinaria.veterinarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "veterinarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioModel { // <-- ¡Asegúrate de poner la palabra 'class' aquí!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veterinario")
    private Long idVeterinario;

    @Column(name = "nombre_vet", nullable = false)
    private String nombreVet;

    private String especialidad;

    private String telefono;
}