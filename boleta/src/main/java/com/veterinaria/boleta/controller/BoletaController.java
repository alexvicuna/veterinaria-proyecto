package com.veterinaria.boleta.controller;

import com.veterinaria.boleta.dto.BoletaDTO;
import com.veterinaria.boleta.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boletas")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;


    @PostMapping
    public ResponseEntity<BoletaDTO> crearBoleta(@RequestBody BoletaDTO dto) {
        BoletaDTO nuevaBoleta = boletaService.crearBoleta(dto);
        return new ResponseEntity<>(nuevaBoleta, HttpStatus.CREATED);
    }
}