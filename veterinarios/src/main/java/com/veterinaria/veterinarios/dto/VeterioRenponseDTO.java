package com.veterinaria.veterinarios.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"idVeterinario", "nombreVet", "especialidad", "telefono"})
public class VeterioRenponseDTO {

    private Long idVeterinario;
    private String nombreVet;
    private String especialidad;
    private String telefono;
}