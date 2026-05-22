package com.veterinaria.historiales_medicos.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder

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
