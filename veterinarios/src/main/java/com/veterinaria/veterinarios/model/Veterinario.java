package com.veterinaria.veterinarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "veterinario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veterinario")
    private Long idVeterinario;
    private String rutVet;

    @Column(name = "nombre_vet", nullable = false)
    private String nombreVet;
    private String apellidoVet;
    private String especialidad;
    private String telefono;
    private String correo;
}
