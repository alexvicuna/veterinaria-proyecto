package com.veterinaria.historiales_medicos.controller;

import com.veterinaria.historiales_medicos.dto.HistorialDTO;
import com.veterinaria.historiales_medicos.service.HistorialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historiales")
public class HistorialController {

    @Autowired
    private HistorialService historialService; // <-- Conecta directo con tu Service

    @PostMapping
    public ResponseEntity<HistorialDTO> crearHistorial(@Valid @RequestBody HistorialDTO dto) {
        return new ResponseEntity<>(historialService.crearHistorial(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HistorialDTO>> obtenerTodos() {
        return ResponseEntity.ok(historialService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialDTO> actualizarHistorial(@PathVariable Long id, @Valid @RequestBody HistorialDTO dto) {
        return ResponseEntity.ok(historialService.actualizarHistorial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHistorial(@PathVariable Long id) {
        historialService.eliminarHistorial(id);
        return ResponseEntity.noContent().build();
    }
}