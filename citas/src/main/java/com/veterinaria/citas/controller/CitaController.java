package com.veterinaria.citas.controller;

import com.veterinaria.citas.dto.ActualizarEstadoDTO;
import com.veterinaria.citas.dto.CitaRequestDTO;
import com.veterinaria.citas.dto.CitaResponseDTO;
import com.veterinaria.citas.service.CitaService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
@Tag(name = "Citas", description = "Operaciones para la gestión de citas veterinarias")
public class CitaController {

    @Autowired
    private CitaService citaService;

    // ──────────────────────────────────────────────
    // POST /api/v1/citas
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Registrar una nueva cita",
            description = "Crea una nueva cita veterinaria. El estado se asigna automáticamente como PENDIENTE."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cita registrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CitaResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "idCita": 1,
                                      "fechaCita": "2026-08-01T10:00:00",
                                      "motivoConsulta": "Control general",
                                      "estadoCita": "PENDIENTE",
                                      "mascota": { "idMascota": 1, "nombreMasc": "Firulais" },
                                      "dueno": { "idDueno": 1, "nombre": "Carlos" },
                                      "veterinario": { "idVeterinario": 1, "nombreVet": "Ana" }
                                    }
                                    """))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "La fecha de la cita debe ser en el futuro" }
                                    """)))
    })
    @PostMapping
    public ResponseEntity<CitaResponseDTO> registrarCita(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva cita",
                    required = true,
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "fechaCita": "2026-08-01T10:00:00",
                              "motivoConsulta": "Control general",
                              "estadoCita": "PENDIENTE",
                              "idMascota": 1,
                              "idDueno": 1,
                              "idVeterinario": 1
                            }
                            """))
            )
            @Valid @RequestBody CitaRequestDTO citaRequestDTO) {
        CitaResponseDTO nuevaCita = citaService.registrarCita(citaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/citas
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener todas las citas",
            description = "Retorna la lista completa de citas registradas con información de mascota, dueño y veterinario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de citas obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CitaResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(citaService.obtenerTodas());
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/citas/{id}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener cita por ID",
            description = "Busca y retorna una cita específica por su identificador único"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CitaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Cita no encontrada con el ID: 99" }
                                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la cita", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/citas/mascota/{idMascota}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener citas por mascota",
            description = "Retorna todas las citas asociadas a una mascota específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de citas de la mascota",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CitaResponseDTO.class)))
    })
    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<CitaResponseDTO>> obtenerPorMascota(
            @Parameter(description = "ID de la mascota", example = "1", required = true)
            @PathVariable Long idMascota) {
        return ResponseEntity.ok(citaService.obtenerPorMascota(idMascota));
    }

    // ──────────────────────────────────────────────
    // GET /api/v1/citas/fecha/{fecha}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Obtener citas por fecha",
            description = "Retorna todas las citas programadas para una fecha específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de citas de la fecha indicada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CitaResponseDTO.class)))
    })
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<CitaResponseDTO>> obtenerPorFecha(
            @Parameter(description = "Fecha en formato YYYY-MM-DD", example = "2026-08-01", required = true)
            @PathVariable LocalDate fecha) {
        return ResponseEntity.ok(citaService.obtenerPorFecha(fecha));
    }

    // ──────────────────────────────────────────────
    // PUT /api/v1/citas/{id}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Actualizar una cita",
            description = "Actualiza los datos de una cita existente (fecha, motivo, mascota, dueño, veterinario)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CitaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Cita no encontrada con el ID: 99" }
                                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> actualizarCita(
            @Parameter(description = "ID de la cita a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CitaRequestDTO citaRequestDTO) {
        return ResponseEntity.ok(citaService.actualizarCita(id, citaRequestDTO));
    }

    // ──────────────────────────────────────────────
    // PATCH /api/v1/citas/{id}/estado
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Actualizar estado de una cita",
            description = "Cambia el estado de una cita. Estados posibles: PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CitaResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "idCita": 1,
                                      "estadoCita": "CONFIRMADA"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Cita no encontrada con el ID: 99" }
                                    """)))
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CitaResponseDTO> actualizarEstado(
            @Parameter(description = "ID de la cita", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevo estado de la cita",
                    content = @Content(examples = @ExampleObject(value = """
                            { "estadoCita": "CONFIRMADA" }
                            """))
            )
            @Valid @RequestBody ActualizarEstadoDTO dto) {
        return ResponseEntity.ok(citaService.actualizarEstado(id, dto));
    }

    // ──────────────────────────────────────────────
    // DELETE /api/v1/citas/{id}
    // ──────────────────────────────────────────────

    @Operation(
            summary = "Eliminar una cita",
            description = "Elimina permanentemente una cita del sistema por su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cita eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "error": "Cita no encontrada con el ID: 99" }
                                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(
            @Parameter(description = "ID de la cita a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}