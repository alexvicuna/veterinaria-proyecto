package com.veterinaria.citas.controller;

import com.veterinaria.citas.dto.CitaRequestDTO;
import com.veterinaria.citas.dto.CitaResponseDTO;
import com.veterinaria.citas.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")

public class CitaController {
    @Autowired
    private CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaResponseDTO> registrarCita(@Valid @RequestBody CitaRequestDTO citaRequestDTO) {
        CitaResponseDTO nuevaCita = citaService.registrarCita(citaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }


    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(citaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<CitaResponseDTO>> obtenerPorMascota(@PathVariable Long idMascota) {
        return ResponseEntity.ok(citaService.obtenerPorMascota(idMascota));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<CitaResponseDTO>> obtenerPorFecha(@PathVariable LocalDate fecha) {
        return ResponseEntity.ok(citaService.obtenerPorFecha(fecha));
    }



    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> actualizarCita(@PathVariable Long id, @Valid @RequestBody CitaRequestDTO citaRequestDTO) {
        return ResponseEntity.ok(citaService.actualizarCita(id, citaRequestDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

}
