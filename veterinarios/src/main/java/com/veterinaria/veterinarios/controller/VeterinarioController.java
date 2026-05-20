package com.veterinaria.veterinarios.controller;


import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
import com.veterinaria.veterinarios.dto.VeterioRenponseDTO;
import com.veterinaria.veterinarios.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarios") // URL base para Postman
public class VeterinarioController {

    @Autowired
    private VeterinarioService iVeterinarioService;


    @PostMapping
    public ResponseEntity<VeterioRenponseDTO> crearVeterinario(@Valid @RequestBody VeterinarioRequestDTO dto) {
        VeterioRenponseDTO nuevo = iVeterinarioService.guardarVeterinario(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VeterioRenponseDTO>> listarVeterinarios() {
        return ResponseEntity.ok(iVeterinarioService.obtenerTodos());
    }
}