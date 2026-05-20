package com.veterinaria.duenos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class DuenoResponseDTO {

        private Long idDueno;
        private String nombre;
        private String apellido;
        private String telefono;
        private String correo;

}
