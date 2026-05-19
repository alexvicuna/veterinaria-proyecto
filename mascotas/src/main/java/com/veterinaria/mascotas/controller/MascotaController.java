package com.veterinaria.mascotas.controller;

import com.veterinaria.mascotas.dto.MascotaRequestDTO;
import com.veterinaria.mascotas.dto.MascotaResponseDTO;
import com.veterinaria.mascotas.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @PostMapping
    public ResponseEntity<MascotaResponseDTO> crearMascota(@Valid @RequestBody MascotaRequestDTO request) {
        return new ResponseEntity<>(mascotaService.crearMascota(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(mascotaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody MascotaRequestDTO request) {
        return ResponseEntity.ok(mascotaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<MascotaResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(mascotaService.buscarPorNombre(nombre));
    }

    @GetMapping("/buscar/raza")
    public ResponseEntity<List<MascotaResponseDTO>> buscarPorRaza(@RequestParam String raza) {
        return ResponseEntity.ok(mascotaService.buscarPorRaza(raza));
    }

    @GetMapping("/buscar/especie")
    public ResponseEntity<List<MascotaResponseDTO>> buscarPorEspecie(@RequestParam String especie) {
        return ResponseEntity.ok(mascotaService.buscarPorEspecie(especie));
    }
}