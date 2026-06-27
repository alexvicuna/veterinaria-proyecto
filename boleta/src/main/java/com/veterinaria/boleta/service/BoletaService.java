package com.veterinaria.boleta.service;

import com.veterinaria.boleta.boletaException.BoletaNotFoundException;
import com.veterinaria.boleta.dto.*;
import com.veterinaria.boleta.model.Boleta;
import com.veterinaria.boleta.model.DetalleBoleta;
import com.veterinaria.boleta.repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service


public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    public BoletaResponseDTO crearBoleta(BoletaRequestDTO dto) {
        // Regla de negocio: la fecha no puede ser futura
        if (dto.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La fecha de la boleta no puede ser una fecha futura");
        }

        // Regla de negocio: debe tener al menos un detalle
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "La boleta debe tener al menos un detalle");
        }

        // Regla de negocio: subtotal de cada detalle debe ser mayor a 0
        dto.getDetalles().forEach(d -> {
            if (d.getSubtotal() <= 0) {
                throw new IllegalArgumentException(
                        "El subtotal de cada detalle debe ser mayor a 0");
            }
        });

        Boleta boleta = new Boleta();
        boleta.setFecha(dto.getFecha());
        boleta.setIdCita(dto.getIdCita());
        boleta.setIdPago(dto.getIdPago());

        List<DetalleBoleta> detalles = dto.getDetalles().stream().map(d -> {
            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setDescripcion(d.getDescripcion());
            detalle.setCantidad(d.getCantidad());
            detalle.setSubtotal(d.getSubtotal());
            detalle.setBoleta(boleta);
            return detalle;
        }).collect(Collectors.toList());

        boleta.setDetalles(detalles);
        boleta.setTotal(detalles.stream().mapToDouble(DetalleBoleta::getSubtotal).sum());

        return toResponse(boletaRepository.save(boleta));
    }

    public BoletaResponseDTO obtenerPorId(Long id) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new BoletaNotFoundException(id));
        return toResponse(boleta);
    }

    public List<BoletaResponseDTO> listarTodos() {
        return boletaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BoletaResponseDTO actualizar(Long id, BoletaRequestDTO dto) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new BoletaNotFoundException(id));

        // Regla de negocio: la fecha no puede ser futura al actualizar
        if (dto.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La fecha de la boleta no puede ser una fecha futura");
        }

        boleta.setFecha(dto.getFecha());
        boleta.setIdCita(dto.getIdCita());
        boleta.setIdPago(dto.getIdPago());

        if (dto.getDetalles() != null) {
            boleta.getDetalles().clear();
            List<DetalleBoleta> detalles = dto.getDetalles().stream().map(d -> {
                DetalleBoleta detalle = new DetalleBoleta();
                detalle.setDescripcion(d.getDescripcion());
                detalle.setCantidad(d.getCantidad());
                detalle.setSubtotal(d.getSubtotal());
                detalle.setBoleta(boleta);
                return detalle;
            }).collect(Collectors.toList());
            boleta.getDetalles().addAll(detalles);
            boleta.setTotal(detalles.stream().mapToDouble(DetalleBoleta::getSubtotal).sum());
        }

        return toResponse(boletaRepository.save(boleta));
    }

    public void eliminarBoleta(Long id) {
        if (!boletaRepository.existsById(id)) {
            throw new BoletaNotFoundException(id);
        }
        boletaRepository.deleteById(id);
    }

    public List<BoletaResponseDTO> listarPorFecha(LocalDate fecha) {
        return boletaRepository.findByFecha(fecha).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BoletaResponseDTO> listarPorRangoDeFechas(LocalDate inicio, LocalDate fin) {
        // Regla de negocio: inicio no puede ser posterior a fin
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException(
                    "La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return boletaRepository.findByFechaBetween(inicio, fin).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BoletaResponseDTO toResponse(Boleta boleta) {
        List<DetalleBoletaResponseDTO> detallesDTO = boleta.getDetalles() == null ? List.of() :
                boleta.getDetalles().stream().map(d -> {
                    DetalleBoletaResponseDTO detalleDTO = new DetalleBoletaResponseDTO();
                    detalleDTO.setIdDetalle(d.getIdDetalle());
                    detalleDTO.setDescripcion(d.getDescripcion());
                    detalleDTO.setCantidad(d.getCantidad());
                    detalleDTO.setSubtotal(d.getSubtotal());
                    return detalleDTO;
                }).collect(Collectors.toList());

        BoletaResponseDTO dto = new BoletaResponseDTO();
        dto.setIdBoleta(boleta.getIdBoleta());
        dto.setFecha(boleta.getFecha());
        dto.setTotal(boleta.getTotal());
        dto.setDetalles(detallesDTO);
        return dto;
    }
}