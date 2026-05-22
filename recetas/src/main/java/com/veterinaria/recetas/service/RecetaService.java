package com.veterinaria.recetas.service;

import com.veterinaria.recetas.dto.RecetaRequestDTO;
import com.veterinaria.recetas.dto.RecetaResponseDTO;
import com.veterinaria.recetas.model.Receta;
import com.veterinaria.recetas.recetasException.RecetaNotFoundException;
import com.veterinaria.recetas.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public RecetaResponseDTO crearReceta(RecetaRequestDTO dto) {
        Receta receta = new Receta();
        receta.setFechaEmision(dto.getFechaEmision());
        receta.setDiagnostico(dto.getDiagnostico());
        receta.setMedicamento(dto.getMedicamento());
        receta.setDosis(dto.getDosis());
        receta.setIdVeterinario(dto.getIdVeterinario());
        receta.setIdMascota(dto.getIdMascota());
        return mappearaDTO(recetaRepository.save(receta));
    }


    public List<RecetaResponseDTO> obtenerTodos() {
        return recetaRepository.findAll().stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public RecetaResponseDTO obtenerPorId(Long id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RecetaNotFoundException(id));
        return mappearaDTO(receta);
    }

    public List<RecetaResponseDTO> obtenerPorMascota(Long idMascota) {
        return recetaRepository.findByIdMascota(idMascota).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }


    public List<RecetaResponseDTO> obtenerPorVeterinario(Long idVeterinario) {
        return recetaRepository.findByIdVeterinario(idVeterinario).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public List<RecetaResponseDTO> obtenerPorMascotaYVeterinario(Long idMascota, Long idVeterinario) {
        return recetaRepository.findByIdMascotaAndIdVeterinario(idMascota, idVeterinario).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }


    public List<RecetaResponseDTO> obtenerPorRangoDeFechas(LocalDate inicio, LocalDate fin) {
        return recetaRepository.findByFechaEmisionBetween(inicio, fin).stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }


    public RecetaResponseDTO actualizarReceta(Long id, RecetaRequestDTO dto) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RecetaNotFoundException(id));

        receta.setFechaEmision(dto.getFechaEmision());
        receta.setDiagnostico(dto.getDiagnostico());
        receta.setMedicamento(dto.getMedicamento());
        receta.setDosis(dto.getDosis());
        receta.setIdVeterinario(dto.getIdVeterinario());
        receta.setIdMascota(dto.getIdMascota());

        return mappearaDTO(recetaRepository.save(receta));
    }

    public void eliminarReceta(Long id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RecetaNotFoundException(id));
        recetaRepository.delete(receta);
    }

    private RecetaResponseDTO mappearaDTO(Receta receta) {
        RecetaResponseDTO dto = new RecetaResponseDTO();
        dto.setIdReceta(receta.getIdReceta());
        dto.setFechaEmision(receta.getFechaEmision());
        dto.setDiagnostico(receta.getDiagnostico());
        dto.setMedicamento(receta.getMedicamento());
        dto.setDosis(receta.getDosis());
        dto.setIdVeterinario(receta.getIdVeterinario());
        dto.setIdMascota(receta.getIdMascota());
        return dto;
    }
}