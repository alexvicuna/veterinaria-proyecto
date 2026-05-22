package com.veterinaria.inventario.controller;

import com.veterinaria.inventario.dto.InventarioRequestDTO;
import com.veterinaria.inventario.dto.InventarioResponseDTO;
import com.veterinaria.inventario.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crearProducto(@RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crearProducto(dto));
    }

    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(inventarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }

    @GetMapping("/nombre/{nombreProducto}")
    public ResponseEntity<List<InventarioResponseDTO>> obtenerPorNombre(@PathVariable String nombreProducto) {
        return ResponseEntity.ok(inventarioService.obtenerPorNombre(nombreProducto));
    }

    @GetMapping("/categoria/{categoriaProducto}")
    public ResponseEntity<List<InventarioResponseDTO>> obtenerPorCategoria(@PathVariable String categoriaProducto) {
        return ResponseEntity.ok(inventarioService.obtenerPorCategoria(categoriaProducto));
    }

    @GetMapping("/stock-bajo/{cantidad}")
    public ResponseEntity<List<InventarioResponseDTO>> obtenerStockBajo(@PathVariable Integer cantidad) {
        return ResponseEntity.ok(inventarioService.obtenerStockBajo(cantidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> actualizarProducto(@PathVariable Long id,
                                                                    @RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.ok(inventarioService.actualizarProducto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        inventarioService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}

