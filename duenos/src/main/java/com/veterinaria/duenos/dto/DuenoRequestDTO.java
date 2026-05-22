package com.veterinaria.duenos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class DuenoRequestDTO {

    private Long idDueno;

    @NotBlank(message = "El RUT no puede estar vacío")
    @Pattern(
            regexp = "^[0-9]{7,8}-[0-9Kk]$",
            message = "El RUT debe tener el formato 12345678-9")
    private String rut;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(
            regexp = "^9\\d{8}$",
            message = "El teléfono debe tener el formato: 912345678")
    private String telefono;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Email debe ser un email válido")
    private String correo;

}
