package com.veterinaria.mascotas.controller;

import com.veterinaria.mascotas.dto.MascotaDTO;
import com.veterinaria.mascotas.dto.MascotaRequestDTO;
import com.veterinaria.mascotas.dto.MascotaResponseDTO;
import com.veterinaria.mascotas.service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mascotas")
@RequiredArgsConstructor
@Tag(name = "Mascotas", description = "Operaciones para la gestión de mascotas en la veterinaria")
public class MascotaController {

    private final MascotaService mascotaService;

    @Operation(
            summary = "Crear una nueva mascota",
            description = "Registra una nueva mascota en el sistema asociándola a un dueño existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Mascota creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MascotaResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "idMascota": 1,
                                      "nombreMasc": "Firulais",
                                      "especie": "Perro",
                                      "raza": "Labrador",
                                      "edad": 3,
                                      "dueno": {
                                        "idDueno": 1,
                                        "nombre": "Carlos",
                                        "apellido": "Pérez"
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "El nombre de la mascota es obligatorio" }
                                    """))
            )
    })
    @PostMapping
    public ResponseEntity<MascotaResponseDTO> crearMascota(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva mascota",
                    required = true,
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "nombreMasc": "Firulais",
                              "especie": "Perro",
                              "raza": "Labrador",
                              "edad": 3,
                              "idDueno": 1
                            }
                            """))
            )
            @Valid @RequestBody MascotaRequestDTO request) {
        return new ResponseEntity<>(mascotaService.crearMascota(request), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtener todas las mascotas",
            description = "Retorna la lista completa de mascotas registradas con la información de su dueño"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de mascotas obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MascotaResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<MascotaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(mascotaService.obtenerTodas());
    }

    @Operation(
            summary = "Obtener mascota por ID",
            description = "Busca y retorna una mascota específica por su identificador único"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mascota encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MascotaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mascota no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "No se encontró una mascota con el ID: 99" }
                                    """))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la mascota", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.obtenerPorId(id));
    }

    @Operation(
            summary = "Actualizar una mascota",
            description = "Actualiza los datos de una mascota existente identificada por su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MascotaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "No se encontró una mascota con el ID: 99" }
                                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> actualizar(
            @Parameter(description = "ID de la mascota a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequestDTO request) {
        return ResponseEntity.ok(mascotaService.actualizar(id, request));
    }

    @Operation(
            summary = "Eliminar una mascota",
            description = "Elimina permanentemente una mascota del sistema por su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mascota eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "No se encontró una mascota con el ID: 99" }
                                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la mascota a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        mascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Buscar mascotas por nombre",
            description = "Retorna todas las mascotas cuyo nombre contenga el texto ingresado (sin distinción de mayúsculas)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MascotaResponseDTO.class)))
    })
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<MascotaResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre o fragmento del nombre a buscar", example = "firu", required = true)
            @RequestParam String nombre) {
        return ResponseEntity.ok(mascotaService.buscarPorNombre(nombre));
    }


    @Operation(
            summary = "Buscar mascotas por raza",
            description = "Retorna todas las mascotas que pertenezcan a la raza indicada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MascotaResponseDTO.class)))
    })
    @GetMapping("/buscar/raza")
    public ResponseEntity<List<MascotaResponseDTO>> buscarPorRaza(
            @Parameter(description = "Raza a buscar", example = "Labrador", required = true)
            @RequestParam String raza) {
        return ResponseEntity.ok(mascotaService.buscarPorRaza(raza));
    }

    @Operation(
            summary = "Buscar mascotas por especie",
            description = "Retorna todas las mascotas de la especie indicada (Perro, Gato, etc.)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MascotaResponseDTO.class)))
    })
    @GetMapping("/buscar/especie")
    public ResponseEntity<List<MascotaResponseDTO>> buscarPorEspecie(
            @Parameter(description = "Especie a buscar", example = "Perro", required = true)
            @RequestParam String especie) {
        return ResponseEntity.ok(mascotaService.buscarPorEspecie(especie));
    }


    @Operation(
            summary = "Obtener mascotas por dueño",
            description = "Retorna la lista de mascotas pertenecientes a un dueño específico. " +
                    "Usa un DTO simplificado para evitar dependencia circular con el microservicio de dueños."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de mascotas del dueño",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MascotaDTO.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "idMascota": 1,
                                        "nombreMasc": "Firulais",
                                        "especie": "Perro",
                                        "raza": "Labrador",
                                        "edad": 3
                                      }
                                    ]
                                    """)))
    })
    @GetMapping("/dueno/{idDueno}")
    public ResponseEntity<List<MascotaDTO>> obtenerPorDueno(
            @Parameter(description = "ID del dueño", example = "1", required = true)
            @PathVariable Long idDueno) {
        return ResponseEntity.ok(mascotaService.buscarPorDueno(idDueno));
    }
}