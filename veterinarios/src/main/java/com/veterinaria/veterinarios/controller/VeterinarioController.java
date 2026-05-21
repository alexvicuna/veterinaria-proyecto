package com.veterinaria.veterinarios.controller;

import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
import com.veterinaria.veterinarios.dto.VeterinarioResponseDTO;
import com.veterinaria.veterinarios.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/V1/veterinarios") // <-- La ruta base para tu API de veterinarios
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    @PostMapping
    public ResponseEntity<VeterinarioResponseDTO> crearVeterinario(@Valid @RequestBody VeterinarioRequestDTO dto) {
        return new ResponseEntity<>(veterinarioService.crearVeterinario(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VeterinarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(veterinarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDTO> actualizarVeterinario(@PathVariable Long id, @Valid @RequestBody VeterinarioRequestDTO dto) {
        return ResponseEntity.ok(veterinarioService.actualizarVeterinario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVeterinario(@PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
        return ResponseEntity.noContent().build();
    }
}