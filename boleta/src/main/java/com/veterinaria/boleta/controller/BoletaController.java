package com.veterinaria.boleta.controller;

import com.veterinaria.boleta.dto.BoletaRequestDTO;
import com.veterinaria.boleta.dto.BoletaResponseDTO;
import com.veterinaria.boleta.service.BoletaService;
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
public class BoletaController {

    private final BoletaService boletaService;

    @GetMapping
    public ResponseEntity<List<BoletaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(boletaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(boletaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<BoletaResponseDTO> crear(@Valid @RequestBody BoletaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BoletaRequestDTO dto) {
        return ResponseEntity.ok(boletaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boletaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<BoletaResponseDTO>> listarPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(boletaService.listarPorFecha(fecha));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<BoletaResponseDTO>> listarPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(boletaService.listarPorRangoDeFechas(inicio, fin));
    }
}