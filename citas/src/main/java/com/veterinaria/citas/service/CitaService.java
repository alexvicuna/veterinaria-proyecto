package com.veterinaria.citas.service;

import com.veterinaria.citas.dto.CitaDTO;
import com.veterinaria.citas.model.Cita;
import com.veterinaria.citas.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class CitaService {
    @Autowired
    private CitaRepository citaRepository;

    // 1. Crear Cita
    public CitaDTO crearCita(CitaDTO citaDTO) {
        Cita cita = mapToEntity(citaDTO);
        Cita nuevaCita = citaRepository.save(cita);
        return mapToDTO(nuevaCita);
    }

    // 2. Obtener Todas las Citas
    public List<CitaDTO> obtenerTodas() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // 3. Obtener por ID
    public CitaDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el ID: " + id));
        return mapToDTO(cita);
    }

    // 4. Actualizar Cita
    public CitaDTO actualizarCita(Long id, CitaDTO citaDTO) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el ID: " + id));

        cita.setFecha(citaDTO.getFecha());
        cita.setMotivos(citaDTO.getMotivos());
        cita.setIdMascota(citaDTO.getIdMascota());
        cita.setIdDueno(citaDTO.getIdDueno());

        Cita citaActualizada = citaRepository.save(cita);
        return mapToDTO(citaActualizada);
    }

    // 5. Eliminar Cita
    public void eliminarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el ID: " + id));
        citaRepository.delete(cita);
    }

    // --- MÉTODOS AUXILIARES DE CONVERSIÓN (Mappers) ---

    private CitaDTO mapToDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setFecha(cita.getFecha());
        dto.setMotivos(cita.getMotivos());
        dto.setIdMascota(cita.getIdMascota());
        dto.setIdDueno(cita.getIdDueno());
        return dto;
    }

    private Cita mapToEntity(CitaDTO dto) {
        Cita cita = new Cita();
        cita.setIdCita(dto.getIdCita());
        cita.setFecha(dto.getFecha());
        cita.setMotivos(dto.getMotivos());
        cita.setIdMascota(dto.getIdMascota());
        cita.setIdDueno(dto.getIdDueno());
        return cita;
    }
}
