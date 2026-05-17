package com.veterinaria.citas.controller;

import com.veterinaria.citas.dto.CitaDTO;
import com.veterinaria.citas.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")

public class CitaController {
    @Autowired
    private CitaService citaService;

    // 1. CREAR UNA NUEVA CITA (Usa @Valid para activar las validaciones del DTO)
    @PostMapping
    public ResponseEntity<CitaDTO> crearCita(@Valid @RequestBody CitaDTO citaDTO) {
        CitaDTO nuevaCita = citaService.crearCita(citaDTO);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    // 2. OBTENER TODAS LAS CITAS
    @GetMapping
    public ResponseEntity<List<CitaDTO>> obtenerTodas() {
        List<CitaDTO> citas = citaService.obtenerTodas();
        return ResponseEntity.ok(citas);
    }

    // 3. OBTENER CITA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> obtenerPorId(@PathVariable Long id) {
        CitaDTO citaDTO = citaService.obtenerPorId(id);
        return ResponseEntity.ok(citaDTO);
    }

    // 4. ACTUALIZAR UNA CITA
    @PutMapping("/{id}")
    public ResponseEntity<CitaDTO> actualizarCita(@PathVariable Long id, @Valid @RequestBody CitaDTO citaDTO) {
        CitaDTO citaActualizada = citaService.actualizarCita(id, citaDTO);
        return ResponseEntity.ok(citaActualizada);
    }

    // 5. ELIMINAR UNA CITA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

}
