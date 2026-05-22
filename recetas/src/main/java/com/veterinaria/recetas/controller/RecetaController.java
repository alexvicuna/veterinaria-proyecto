package com.veterinaria.recetas.controller;

import com.veterinaria.recetas.dto.RecetaRequestDTO;
import com.veterinaria.recetas.dto.RecetaResponseDTO;
import com.veterinaria.recetas.service.RecetaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @PostMapping
    public ResponseEntity<RecetaResponseDTO> crearReceta(@Valid @RequestBody RecetaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recetaService.crearReceta(dto));
    }

    @GetMapping
    public ResponseEntity<List<RecetaResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(recetaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recetaService.obtenerPorId(id));
    }

    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<RecetaResponseDTO>> obtenerPorMascota(@PathVariable Long idMascota) {
        return ResponseEntity.ok(recetaService.obtenerPorMascota(idMascota));
    }

    @GetMapping("/veterinario/{idVeterinario}")
    public ResponseEntity<List<RecetaResponseDTO>> obtenerPorVeterinario(@PathVariable Long idVeterinario) {
        return ResponseEntity.ok(recetaService.obtenerPorVeterinario(idVeterinario));
    }

    @GetMapping("/mascota/{idMascota}/veterinario/{idVeterinario}")
    public ResponseEntity<List<RecetaResponseDTO>> obtenerPorMascotaYVeterinario(
            @PathVariable Long idMascota,
            @PathVariable Long idVeterinario) {
        return ResponseEntity.ok(recetaService.obtenerPorMascotaYVeterinario(idMascota, idVeterinario));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<RecetaResponseDTO>> obtenerPorRangoDeFechas(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {
        return ResponseEntity.ok(recetaService.obtenerPorRangoDeFechas(inicio, fin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecetaResponseDTO> actualizarReceta(@PathVariable Long id,
                                                              @Valid @RequestBody RecetaRequestDTO dto) {
        return ResponseEntity.ok(recetaService.actualizarReceta(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReceta(@PathVariable Long id) {
        recetaService.eliminarReceta(id);
        return ResponseEntity.noContent().build();
    }
}