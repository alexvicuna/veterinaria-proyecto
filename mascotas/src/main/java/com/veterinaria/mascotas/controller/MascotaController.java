package com.veterinaria.mascotas.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.veterinaria.mascotas.dto.MascotaDTO;
import com.veterinaria.mascotas.service.MascotaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mascotas")


public class MascotaController {

    @Autowired
    private MascotaService mascotaService;


    @PostMapping
    public ResponseEntity<MascotaDTO> crearMascota(@Valid @RequestBody MascotaDTO mascotaDTO) {
        MascotaDTO nuevaMascota = mascotaService.registrarMascota(mascotaDTO);
        return new ResponseEntity<>(nuevaMascota, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<MascotaDTO>> obtenerTodas() {
        return new ResponseEntity<>(mascotaService.obtenerTodas(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MascotaDTO> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(mascotaService.obtenerPorId(id), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<MascotaDTO> actualizarMascota(@PathVariable Long id, @Valid @RequestBody MascotaDTO mascotaDTO) {
        MascotaDTO mascotaActualizada = mascotaService.actualizarMascota(id, mascotaDTO);
        return new ResponseEntity<>(mascotaActualizada, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascota(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }

}


