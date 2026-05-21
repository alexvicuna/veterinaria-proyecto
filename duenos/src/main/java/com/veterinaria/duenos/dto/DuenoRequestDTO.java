package com.veterinaria.duenos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class DuenoRequestDTO {

    private Long idDueno;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;
    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Email debe ser un email válido")
    private String correo;

}
