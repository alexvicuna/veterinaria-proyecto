package com.veterinaria.historiales_medicos.service;

import com.veterinaria.historiales_medicos.dto.HistorialDTO;
import com.veterinaria.historiales_medicos.model.Historial;
import com.veterinaria.historiales_medicos.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    public HistorialDTO crearHistorial(HistorialDTO dto) {
        Historial historial = mapToEntity(dto);
        return mapToDTO(historialRepository.save(historial));
    }

    public List<HistorialDTO> obtenerTodos() {
        return historialRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HistorialDTO obtenerPorId(Long id) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado"));
        return mapToDTO(historial);
    }

    public HistorialDTO actualizarHistorial(Long id, HistorialDTO dto) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado"));
        historial.setDiagnostico(dto.getDiagnostico());
        historial.setTratamiento(dto.getTratamiento());
        historial.setVacunas(dto.getVacunas());
        historial.setIdMascota(dto.getIdMascota());
        return mapToDTO(historialRepository.save(historial));
    }

    public void eliminarHistorial(Long id) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado"));
        historialRepository.delete(historial);
    }

    private HistorialDTO mapToDTO(Historial historial) {
        HistorialDTO dto = new HistorialDTO();
        dto.setIdHistorial(historial.getIdHistorial());
        dto.setDiagnostico(historial.getDiagnostico());
        dto.setTratamiento(historial.getTratamiento());
        dto.setVacunas(historial.getVacunas());
        dto.setIdMascota(historial.getIdMascota());
        return dto;
    }

    private Historial mapToEntity(HistorialDTO dto) {
        Historial historial = new Historial();
        historial.setIdHistorial(dto.getIdHistorial());
        historial.setDiagnostico(dto.getDiagnostico());
        historial.setTratamiento(dto.getTratamiento());
        historial.setVacunas(dto.getVacunas());
        historial.setIdMascota(dto.getIdMascota());
        return historial;
    }
}