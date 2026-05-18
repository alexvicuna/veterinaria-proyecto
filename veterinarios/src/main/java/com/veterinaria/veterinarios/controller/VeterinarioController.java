package com.veterinaria.veterinarios.controller;

import com.veterinaria.veterinarios.dto.VeterinarioDTO;
import com.veterinaria.veterinarios.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/veterinarios")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;


    @PostMapping
    public ResponseEntity<VeterinarioDTO> crearVeterinario(@Valid @RequestBody VeterinarioDTO dto) {
        return new ResponseEntity<>(veterinarioService.crearVeterinario(dto), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<VeterinarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(veterinarioService.obtenerTodos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.obtenerPorId(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioDTO> actualizarVeterinario(@PathVariable Long id, @Valid @RequestBody VeterinarioDTO dto) {
        return ResponseEntity.ok(veterinarioService.actualizarVeterinario(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVeterinario(@PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
        return ResponseEntity.noContent().build();
    }
}