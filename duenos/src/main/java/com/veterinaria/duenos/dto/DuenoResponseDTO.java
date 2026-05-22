package com.veterinaria.duenos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class DuenoResponseDTO {

        private Long idDueno;
        private String rut;
        private String nombre;
        private String apellido;
        private String telefono;
        private String correo;

        private List<MascotaDTO> mascotas;

}
