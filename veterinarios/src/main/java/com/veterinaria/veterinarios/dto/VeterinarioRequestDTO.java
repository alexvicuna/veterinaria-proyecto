package com.veterinaria.veterinarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioRequestDTO {

    @NotBlank(message = "El RUT no puede estar vacío")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$", message = "El RUT debe tener el formato 12345678-9")
    private String rutVet;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombreVet;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellidoVet;

    @NotBlank(message = "La especialidad no puede estar vacía")
    private String especialidad;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotBlank(message = "El correo no puede estar vacío")
    private String correo;
}