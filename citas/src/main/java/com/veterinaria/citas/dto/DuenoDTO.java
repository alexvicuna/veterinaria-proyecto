package com.veterinaria.citas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DuenoDTO {

    private Long idDueno;
    private String rut;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;

}
