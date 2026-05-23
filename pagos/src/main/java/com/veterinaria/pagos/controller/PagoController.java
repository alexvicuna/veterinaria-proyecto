package com.veterinaria.pagos.controller;

import com.veterinaria.pagos.dto.ActualizarEstadoPagoDTO;
import com.veterinaria.pagos.dto.PagoRequestDTO;
import com.veterinaria.pagos.dto.PagoResponseDTO;
import com.veterinaria.pagos.model.DetallePago;
import com.veterinaria.pagos.model.MetodoPago;
import com.veterinaria.pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrarPago(@Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.registrarPago(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> actualizarPago(@PathVariable Long id,
                                                          @Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.ok(pagoService.actualizarPago(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PagoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoPagoDTO dto) {
        return ResponseEntity.ok(pagoService.actualizarEstado(id, dto.getDetallePago()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoResponseDTO>> listarPorEstado(@PathVariable DetallePago estado) {
        return ResponseEntity.ok(pagoService.listarPorEstado(estado));
    }

    @GetMapping("/metodo/{metodo}")
    public ResponseEntity<List<PagoResponseDTO>> listarPorMetodoPago(@PathVariable MetodoPago metodo) {
        return ResponseEntity.ok(pagoService.listarPorMetodoPago(metodo));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<PagoResponseDTO>> listarPorRangoDeFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(pagoService.listarPorRangoDeFechas(inicio, fin));
    }
}