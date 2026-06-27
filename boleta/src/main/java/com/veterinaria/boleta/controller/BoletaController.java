package com.veterinaria.boleta.controller;

import com.veterinaria.boleta.dto.BoletaRequestDTO;
import com.veterinaria.boleta.dto.BoletaResponseDTO;
import com.veterinaria.boleta.service.BoletaService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/boletas")
@RequiredArgsConstructor
@Tag(name = "Boletas", description = "Gestion de boletas del sistema veterinario")
public class BoletaController {

    private final BoletaService boletaService;

    @Operation(
            summary = "Crear una boleta",
            description = "Registra una nueva boleta con sus detalles. El total se calcula automaticamente sumando los subtotales. La fecha no puede ser futura."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Boleta creada exitosamente",
                    content = @Content(schema = @Schema(implementation = BoletaResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "idBoleta": 1,
                      "fecha": "2025-06-10",
                      "total": 45000.0,
                      "detalles": [
                        { "idDetalle": 1, "descripcion": "Consulta general", "cantidad": 1, "subtotal": 25000.0 },
                        { "idDetalle": 2, "descripcion": "Vacuna antirrábica", "cantidad": 1, "subtotal": 20000.0 }
                      ]
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o fecha futura",
                    content = @Content(examples = @ExampleObject(
                            value = "{\"error\": \"La fecha de la boleta no puede ser una fecha futura\"}")))
    })
    @PostMapping
    public ResponseEntity<BoletaResponseDTO> crear(@Valid @RequestBody BoletaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletaService.crearBoleta(dto));
    }

    @Operation(
            summary = "Listar todas las boletas",
            description = "Retorna la lista completa de boletas registradas con sus detalles"
    )
    @ApiResponse(responseCode = "200", description = "Lista de boletas",
            content = @Content(schema = @Schema(implementation = BoletaResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<BoletaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(boletaService.listarTodos());
    }

    @Operation(
            summary = "Obtener boleta por ID",
            description = "Retorna el detalle completo de una boleta segun su identificador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boleta encontrada",
                    content = @Content(schema = @Schema(implementation = BoletaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada",
                    content = @Content(examples = @ExampleObject(
                            value = "{\"error\": \"Boleta no encontrada con ID: 99\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la boleta", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(boletaService.obtenerPorId(id));
    }

    @Operation(
            summary = "Actualizar boleta",
            description = "Modifica los datos de una boleta existente y recalcula el total automaticamente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boleta actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = BoletaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> actualizar(
            @Parameter(description = "ID de la boleta a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BoletaRequestDTO dto) {
        return ResponseEntity.ok(boletaService.actualizar(id, dto));
    }

    @Operation(
            summary = "Eliminar boleta",
            description = "Elimina de forma permanente una boleta y todos sus detalles asociados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Boleta eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la boleta a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        boletaService.eliminarBoleta(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar boletas por fecha exacta",
            description = "Retorna todas las boletas emitidas en una fecha especifica (formato: yyyy-MM-dd)"
    )
    @ApiResponse(responseCode = "200", description = "Boletas encontradas para la fecha indicada")
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<BoletaResponseDTO>> listarPorFecha(
            @Parameter(description = "Fecha exacta (yyyy-MM-dd)", required = true, example = "2025-06-10")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(boletaService.listarPorFecha(fecha));
    }

    @Operation(
            summary = "Listar boletas por rango de fechas",
            description = "Retorna las boletas emitidas entre inicio y fin inclusive. La fecha de inicio no puede ser posterior a la de fin."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boletas en el rango indicado"),
            @ApiResponse(responseCode = "400", description = "Rango de fechas invalido")
    })
    @GetMapping("/rango")
    public ResponseEntity<List<BoletaResponseDTO>> listarPorRango(
            @Parameter(description = "Fecha inicio (yyyy-MM-dd)", required = true, example = "2025-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Fecha fin (yyyy-MM-dd)", required = true, example = "2025-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(boletaService.listarPorRangoDeFechas(inicio, fin));
    }
}