package com.veterinaria.veterinarios.dto;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"nombreVet", "especialidad", "telefono"})
public class VeterinarioRequestDTO {

    @NotBlank(message = "El nombre del veterinario es obligatorio")
    @Size(min = 1, max = 150, message = "El nombre debe tener entre 1 y 150 caracteres")
    private String nombreVet;

    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    private String especialidad;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;
=======
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
>>>>>>> 0429cfed3641891bf219397071c83ecf49cf9344
}