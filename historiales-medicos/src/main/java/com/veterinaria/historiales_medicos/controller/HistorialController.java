package com.veterinaria.historiales_medicos.controller;

import com.veterinaria.historiales_medicos.dto.HistorialRequestDTO;
import com.veterinaria.historiales_medicos.dto.HistorialResponseDTO;
import com.veterinaria.historiales_medicos.service.HistorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/historiales")
@RequiredArgsConstructor
public class HistorialController {

    private final HistorialService historialService;

    @PostMapping
    public ResponseEntity<HistorialResponseDTO> crearHistorial(@RequestBody HistorialRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(historialService.crearHistorial(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<HistorialResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(historialService.obtenerTodos());
    }

    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<HistorialResponseDTO>> obtenerPorMascota(@PathVariable Long idMascota) {
        return ResponseEntity.ok(historialService.obtenerPorMascota(idMascota));
    }
    @GetMapping("/mascota/{idMascota}/veterinario/{idVeterinario}")
    public ResponseEntity<List<HistorialResponseDTO>> obtenerPorMascotaYVeterinario(
            @PathVariable Long idMascota,
            @PathVariable Long idVeterinario) {
        return ResponseEntity.ok(historialService.obtenerPorMascotaYVeterinario(idMascota, idVeterinario));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<HistorialResponseDTO>> obtenerPorRangoDeFechas(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {
        return ResponseEntity.ok(historialService.obtenerPorRangoDeFechas(inicio, fin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialResponseDTO> actualizarHistorial(@PathVariable Long id,@RequestBody HistorialRequestDTO request) {
        return ResponseEntity.ok(historialService.actualizarHistorial(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHistorial(@PathVariable Long id) {
        historialService.eliminarHistorial(id);
        return ResponseEntity.noContent().build();
    }
}