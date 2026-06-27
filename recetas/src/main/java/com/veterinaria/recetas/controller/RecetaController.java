package com.veterinaria.recetas.controller;

import com.veterinaria.recetas.dto.RecetaRequestDTO;
import com.veterinaria.recetas.dto.RecetaResponseDTO;
import com.veterinaria.recetas.service.RecetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recetas")
@Tag(name = "Recetas", description = "Operaciones para la gestión de recetas médicas veterinarias")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    // ──────────────────────────────────────────────
    // POST /api/v1/recetas
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Crear una nueva receta",
            description = "Registra una nueva receta médica asociada a una mascota, veterinario y cita"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Receta creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecetaResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "idReceta": 1,
                                      "fechaEmision": "2026-07-01",
                                      "diagnostico": "Infección bacteriana",
                                      "medicamento": "Amoxicilina",
                                      "dosis": "500mg cada 8 horas por 7 días",
                                      "mascota": { "idMascota": 1, "nombreMasc": "Firulais" },
                                      "veterinario": { "idVeterinario": 1, "nombreVet": "Ana" },
                                      "cita": { "idCita": 1, "motivoConsulta": "Control general" }
                                    }
                                    """))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "El medicamento es obligatorio" }
                                    """)))
    })
    @PostMapping
    public ResponseEntity<RecetaResponseDTO> crearReceta(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva receta",
                    required = true,
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "fechaEmision": "2026-07-01",
                              "diagnostico": "Infección bacteriana",
                              "medicamento": "Amoxicilina",
                              "dosis": "500mg cada 8 horas por 7 días",
                              "idVeterinario": 1,
                              "idMascota": 1,
                              "idCita": 1
                            }
                            """))
            )
            @Valid @RequestBody RecetaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recetaService.crearReceta(dto));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/recetas
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener todas las recetas",
            description = "Retorna la lista completa de recetas médicas con información de mascota, veterinario y cita"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de recetas obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecetaResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<RecetaResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(recetaService.obtenerTodos());
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/recetas/{id}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener receta por ID",
            description = "Busca y retorna una receta específica por su identificador único"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receta encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecetaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Receta no encontrada con el ID: 99" }
                                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecetaResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la receta", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(recetaService.obtenerPorId(id));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/recetas/mascota/{idMascota}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener recetas por mascota",
            description = "Retorna todas las recetas asociadas a una mascota específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de recetas de la mascota",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecetaResponseDTO.class)))
    })
    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<RecetaResponseDTO>> obtenerPorMascota(
            @Parameter(description = "ID de la mascota", example = "1", required = true)
            @PathVariable Long idMascota) {
        return ResponseEntity.ok(recetaService.obtenerPorMascota(idMascota));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/recetas/veterinario/{idVeterinario}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener recetas por veterinario",
            description = "Retorna todas las recetas emitidas por un veterinario específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de recetas del veterinario",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecetaResponseDTO.class)))
    })
    @GetMapping("/veterinario/{idVeterinario}")
    public ResponseEntity<List<RecetaResponseDTO>> obtenerPorVeterinario(
            @Parameter(description = "ID del veterinario", example = "1", required = true)
            @PathVariable Long idVeterinario) {
        return ResponseEntity.ok(recetaService.obtenerPorVeterinario(idVeterinario));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/recetas/mascota/{idMascota}/veterinario/{idVeterinario}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener recetas por mascota y veterinario",
            description = "Retorna recetas filtradas simultáneamente por mascota y veterinario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de recetas filtradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecetaResponseDTO.class)))
    })
    @GetMapping("/mascota/{idMascota}/veterinario/{idVeterinario}")
    public ResponseEntity<List<RecetaResponseDTO>> obtenerPorMascotaYVeterinario(
            @Parameter(description = "ID de la mascota", example = "1", required = true)
            @PathVariable Long idMascota,
            @Parameter(description = "ID del veterinario", example = "1", required = true)
            @PathVariable Long idVeterinario) {
        return ResponseEntity.ok(recetaService.obtenerPorMascotaYVeterinario(idMascota, idVeterinario));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/recetas/fechas
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener recetas por rango de fechas",
            description = "Retorna todas las recetas emitidas entre dos fechas indicadas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de recetas en el rango de fechas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecetaResponseDTO.class)))
    })
    @GetMapping("/fechas")
    public ResponseEntity<List<RecetaResponseDTO>> obtenerPorRangoDeFechas(
            @Parameter(description = "Fecha inicio (YYYY-MM-DD)", example = "2026-01-01", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Fecha fin (YYYY-MM-DD)", example = "2026-12-31", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(recetaService.obtenerPorRangoDeFechas(inicio, fin));
    }

    // ──────────────────────────────────────────────
    // PUT /api/v1/recetas/{id}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Actualizar una receta",
            description = "Actualiza los datos de una receta existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receta actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecetaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Receta no encontrada con el ID: 99" }
                                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<RecetaResponseDTO> actualizarReceta(
            @Parameter(description = "ID de la receta a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RecetaRequestDTO dto) {
        return ResponseEntity.ok(recetaService.actualizarReceta(id, dto));
    }

    // ──────────────────────────────────────────────
    // DELETE /api/v1/recetas/{id}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Eliminar una receta",
            description = "Elimina permanentemente una receta del sistema por su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Receta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Receta no encontrada con el ID: 99" }
                                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReceta(
            @Parameter(description = "ID de la receta a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        recetaService.eliminarReceta(id);
        return ResponseEntity.noContent().build();
    }
}