package com.veterinaria.veterinarios.controller;

import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
import com.veterinaria.veterinarios.dto.VeterinarioResponseDTO;
import com.veterinaria.veterinarios.service.VeterinarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/veterinarios") // URL base para Postman
@RequiredArgsConstructor
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @PostMapping
    public ResponseEntity<VeterinarioResponseDTO> registrarVeterinario(@Valid @RequestBody VeterinarioRequestDTO dto) {
        return new ResponseEntity<>(veterinarioService.guardarVeterinario(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VeterinarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(veterinarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.obtenerPorId(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<VeterinarioResponseDTO> obtenerPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(veterinarioService.obtenerPorRut(rut));
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<VeterinarioResponseDTO>> obtenerPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(veterinarioService.obtenerPorEspecialidad(especialidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDTO> actualizarVeterinario(@PathVariable Long id,
                                                                        @Valid @RequestBody VeterinarioRequestDTO dto) {
        return ResponseEntity.ok(veterinarioService.actualizarVeterinario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVeterinario(@PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
        return ResponseEntity.noContent().build();
    }
}
