package com.veterinaria.citas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;

    private LocalDateTime fechaCita;
    private String motivoConsulta;

    @Enumerated(EnumType.STRING)
    private EstadoCita estadoCita;

    private Long idMascota;
    private Long idDueno;
    private Long idVeterinario;
}

