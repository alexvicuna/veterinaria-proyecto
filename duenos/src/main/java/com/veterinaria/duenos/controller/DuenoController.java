package com.veterinaria.duenos.controller;

import com.veterinaria.duenos.dto.DuenoRequestDTO;
import com.veterinaria.duenos.dto.DuenoResponseDTO;
import com.veterinaria.duenos.service.DuenoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/duenos")
public class DuenoController {

    @Autowired
    private DuenoService duenoService;

    @PostMapping
    public ResponseEntity<DuenoResponseDTO> registrarDueno(@Valid @RequestBody DuenoRequestDTO duenoDto) {
        DuenoResponseDTO nuevoDueno = duenoService.registrarDueno(duenoDto);
        return new ResponseEntity<>(nuevoDueno, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DuenoResponseDTO>> obtenerTodos() {
        List<DuenoResponseDTO> lista = duenoService.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuenoResponseDTO> obtenerPorId(@PathVariable Long id) {
        DuenoResponseDTO duenoDto = duenoService.obtenerPorId(id);
        return new ResponseEntity<>(duenoDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DuenoResponseDTO> actualizarDueno(@PathVariable Long id, @Valid @RequestBody DuenoRequestDTO duenoDto) {
        DuenoResponseDTO actualizado = duenoService.actualizarDueno(id, duenoDto);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDueno(@PathVariable Long id) {
        duenoService.eliminarDueno(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

