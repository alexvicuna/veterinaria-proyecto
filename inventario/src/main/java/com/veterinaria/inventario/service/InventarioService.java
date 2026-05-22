package com.veterinaria.inventario.service;

import com.veterinaria.inventario.dto.InventarioRequestDTO;
import com.veterinaria.inventario.dto.InventarioResponseDTO;
import com.veterinaria.inventario.inventarioException.InventarioNotFoundException;
import com.veterinaria.inventario.model.Inventario;
import com.veterinaria.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public InventarioResponseDTO crearProducto(InventarioRequestDTO dto) {
        Inventario inventario = new Inventario();
        inventario.setNombreProducto(dto.getNombreProducto());
        inventario.setDescripcion(dto.getDescripcion());
        inventario.setCategoriaProducto(dto.getCategoriaProducto());
        inventario.setCantidadDisponible(dto.getCantidadDisponible());
        return mappearaDTO(inventarioRepository.save(inventario));
    }

    public List<InventarioResponseDTO> obtenerTodos() {
        return inventarioRepository.findAll().stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public InventarioResponseDTO obtenerPorId(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new InventarioNotFoundException(id));
        return mappearaDTO(inventario);
    }


    public List<InventarioResponseDTO> obtenerPorNombre(String nombreProducto) {
        return inventarioRepository.findByNombreProductoContainingIgnoreCase(nombreProducto).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }


    public List<InventarioResponseDTO> obtenerPorCategoria(String categoriaProducto) {
        return inventarioRepository.findByCategoriaProducto(categoriaProducto).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }


    public List<InventarioResponseDTO> obtenerStockBajo(Integer cantidad) {
        return inventarioRepository.findByCantidadDisponibleLessThan(cantidad).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }


    public InventarioResponseDTO actualizarProducto(Long id, InventarioRequestDTO dto) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));

        inventario.setNombreProducto(dto.getNombreProducto());
        inventario.setDescripcion(dto.getDescripcion());
        inventario.setCategoriaProducto(dto.getCategoriaProducto());
        inventario.setCantidadDisponible(dto.getCantidadDisponible());

        return mappearaDTO(inventarioRepository.save(inventario));
    }


    public void eliminarProducto(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
        inventarioRepository.delete(inventario);
    }


    private InventarioResponseDTO mappearaDTO(Inventario inventario) {
        InventarioResponseDTO dto = new InventarioResponseDTO();
        dto.setIdProducto(inventario.getIdProducto());
        dto.setNombreProducto(inventario.getNombreProducto());
        dto.setDescripcion(inventario.getDescripcion());
        dto.setCategoriaProducto(inventario.getCategoriaProducto());
        dto.setCantidadDisponible(inventario.getCantidadDisponible());
        return dto;
    }
}