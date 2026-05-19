package com.veterinaria.mascotas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuenoDTO {

    private Long idDueno;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;


}
