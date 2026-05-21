package com.veterinaria.mascotas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"idDueno", "rut", "nombre", "apellido", "telefono", "correo"})
public class DuenoDTO {

    private Long idDueno;
    private String rut;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;


}
