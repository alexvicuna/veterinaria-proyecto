package com.veterinaria.boleta.service;

import com.veterinaria.boleta.dto.*;
import com.veterinaria.boleta.boletaException.BoletaNotFoundException;
import com.veterinaria.boleta.model.Boleta;
import com.veterinaria.boleta.model.DetalleBoleta;
import com.veterinaria.boleta.repository.BoletaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoletaService {

    private final BoletaRepository boletaRepository;

    public List<BoletaResponseDTO> listarTodos() {
        return boletaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BoletaResponseDTO obtenerPorId(Long id) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new BoletaNotFoundException(id));
        return toResponse(boleta);
    }

    public BoletaResponseDTO crear(BoletaRequestDTO dto) {
        Boleta boleta = new Boleta();
        boleta.setFecha(dto.getFecha());
        boleta.setTotal(dto.getTotal());

        if (dto.getDetalles() != null) {
            List<DetalleBoleta> detalles = dto.getDetalles().stream().map(d -> {
                DetalleBoleta detalle = new DetalleBoleta();
                detalle.setCantidad(d.getCantidad());
                detalle.setSubtotal(d.getSubtotal());
                detalle.setBoleta(boleta);
                return detalle;
            }).collect(Collectors.toList());
            boleta.setDetalles(detalles);
        }

        return toResponse(boletaRepository.save(boleta));
    }

    public BoletaResponseDTO actualizar(Long id, BoletaRequestDTO dto) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new BoletaNotFoundException(id));

        boleta.setFecha(dto.getFecha());
        boleta.setTotal(dto.getTotal());

        if (dto.getDetalles() != null) {
            boleta.getDetalles().clear();
            List<DetalleBoleta> detalles = dto.getDetalles().stream().map(d -> {
                DetalleBoleta detalle = new DetalleBoleta();
                detalle.setCantidad(d.getCantidad());
                detalle.setSubtotal(d.getSubtotal());
                detalle.setBoleta(boleta);
                return detalle;
            }).collect(Collectors.toList());
            boleta.getDetalles().addAll(detalles);
        }

        return toResponse(boletaRepository.save(boleta));
    }

    public void eliminar(Long id) {
        if (!boletaRepository.existsById(id)) {
            throw new BoletaNotFoundException(id);
        }
        boletaRepository.deleteById(id);
    }

    public List<BoletaResponseDTO> listarPorFecha(LocalDate fecha) {
        return boletaRepository.findByFecha(fecha)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BoletaResponseDTO> listarPorRangoDeFechas(LocalDate inicio, LocalDate fin) {
        return boletaRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BoletaResponseDTO toResponse(Boleta boleta) {
        List<DetalleBoletaResponseDTO> detallesDTO = boleta.getDetalles() == null ? List.of() :
                boleta.getDetalles().stream().map(d -> DetalleBoletaResponseDTO.builder()
                                                       .idDetalle(d.getIdDetalle())
                                                       .cantidad(d.getCantidad())
                                                       .subtotal(d.getSubtotal())
                                                       .idBoleta(boleta.getIdBoleta())
                                                       .build()
                ).collect(Collectors.toList());

        return BoletaResponseDTO.builder()
                .idBoleta(boleta.getIdBoleta())
                .fecha(boleta.getFecha())
                .total(boleta.getTotal())
                .detalles(detallesDTO)
                .build();
    }
}