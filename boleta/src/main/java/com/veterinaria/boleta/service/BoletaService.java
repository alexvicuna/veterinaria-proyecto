package com.veterinaria.boleta.service;

import com.veterinaria.boleta.dto.BoletaDTO;
import com.veterinaria.boleta.dto.DetalleBoletaDTO;
import com.veterinaria.boleta.model.Boleta;
import com.veterinaria.boleta.model.DetalleBoleta;
import com.veterinaria.boleta.repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;


    public BoletaDTO crearBoleta(BoletaDTO dto) {
        Boleta boleta = new Boleta();
        boleta.setFecha(dto.getFecha());
        boleta.setTotal(dto.getTotal());

        if (dto.getDetalles() != null) {
            List<DetalleBoleta> detalles = dto.getDetalles().stream().map(detalleDto -> {
                DetalleBoleta detalleEn = new DetalleBoleta();
                detalleEn.setCantidad(detalleDto.getCantidad());
                detalleEn.setSubtotal(detalleDto.getSubtotal());
                detalleEn.setBoleta(boleta); // Enlazamos el detalle con su boleta
                return detalleEn;
            }).collect(Collectors.toList());

            boleta.setDetalles(detalles);
        }


        Boleta boletaGuardada = boletaRepository.save(boleta);
        return mapToDTO(boletaGuardada);
    }


    private BoletaDTO mapToDTO(Boleta boleta) {
        BoletaDTO dto = new BoletaDTO();
        dto.setIdBoleta(boleta.getIdBoleta());
        dto.setFecha(boleta.getFecha());
        dto.setTotal(boleta.getTotal());

        if (boleta.getDetalles() != null) {
            List<DetalleBoletaDTO> detallesDTO = boleta.getDetalles().stream().map(d ->
                    new DetalleBoletaDTO(d.getIdDetalle(), d.getCantidad(), d.getSubtotal())
            ).collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }
        return dto;
    }
}