package com.veterinaria.historiales_medicos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter@Setter@AllArgsConstructor
@NoArgsConstructor

public class HistorialResponseDTO {

        private Long idHistorial;
        private String diagnostico;
        private String tratamiento;
        private String vacunas;
        private LocalDate fechaAtencion;
        private String observaciones;
        private Long idMascota;
        private Long idVeterinario;


}
