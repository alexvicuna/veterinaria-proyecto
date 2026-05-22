package com.veterinaria.historiales_medicos.service;

import com.veterinaria.historiales_medicos.dto.HistorialRequestDTO;
import com.veterinaria.historiales_medicos.dto.HistorialResponseDTO;
import com.veterinaria.historiales_medicos.model.Historial;
import com.veterinaria.historiales_medicos.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    public HistorialResponseDTO crearHistorial(HistorialRequestDTO dto) {
        Historial historial = new Historial();
        historial.setDiagnostico(dto.getDiagnostico());
        historial.setTratamiento(dto.getTratamiento());
        historial.setVacunas(dto.getVacunas());
        historial.setObservaciones(dto.getObservaciones());
        historial.setFechaAtencion(dto.getFechaAtencion());
        historial.setIdMascota(dto.getIdMascota());
        historial.setIdVeterinario(dto.getIdVeterinario());
        return mappearaDTO(historialRepository.save(historial));
    }

    public List<HistorialResponseDTO> obtenerTodos() {
        return historialRepository.findAll().stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public HistorialResponseDTO obtenerPorId(Long id) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con el ID: " + id));
        return mappearaDTO(historial);
    }

    public List<HistorialResponseDTO> obtenerPorMascota(Long idMascota) {
        return historialRepository.findByIdMascota(idMascota).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public List<HistorialResponseDTO> obtenerPorVeterinario(Long idVeterinario) {
        return historialRepository.findByIdVeterinario(idVeterinario).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public HistorialResponseDTO actualizarHistorial(Long id, HistorialRequestDTO dto) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con el ID: " + id));

        historial.setDiagnostico(dto.getDiagnostico());
        historial.setTratamiento(dto.getTratamiento());
        historial.setVacunas(dto.getVacunas());
        historial.setObservaciones(dto.getObservaciones());
        historial.setFechaAtencion(dto.getFechaAtencion());
        historial.setIdMascota(dto.getIdMascota());
        historial.setIdVeterinario(dto.getIdVeterinario());

        return mappearaDTO(historialRepository.save(historial));
    }


    public List<HistorialResponseDTO> obtenerPorMascotaYVeterinario(Long idMascota, Long idVeterinario) {
        return historialRepository.findByIdMascotaAndIdVeterinario(idMascota, idVeterinario).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public List<HistorialResponseDTO> obtenerPorRangoDeFechas(LocalDate inicio, LocalDate fin) {
        return historialRepository.findByFechaAtencionBetween(inicio, fin).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public void eliminarHistorial(Long id) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con el ID: " + id));
        historialRepository.delete(historial);
    }

    private HistorialResponseDTO mappearaDTO(Historial historial) {
        HistorialResponseDTO dto = new HistorialResponseDTO();
        dto.setIdHistorial(historial.getIdHistorial());
        dto.setDiagnostico(historial.getDiagnostico());
        dto.setTratamiento(historial.getTratamiento());
        dto.setVacunas(historial.getVacunas());
        dto.setObservaciones(historial.getObservaciones());
        dto.setFechaAtencion(historial.getFechaAtencion());
        dto.setIdMascota(historial.getIdMascota());
        dto.setIdVeterinario(historial.getIdVeterinario());
        return dto;
    }
}