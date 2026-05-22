package com.veterinaria.citas.service;

import com.veterinaria.citas.client.DuenoClient;
import com.veterinaria.citas.client.MascotaClient;
import com.veterinaria.citas.client.VeterinarioClient;
import com.veterinaria.citas.dto.*;
import com.veterinaria.citas.model.Cita;
import com.veterinaria.citas.model.EstadoCita;
import com.veterinaria.citas.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MascotaClient mascotaClient;

    @Autowired
    private DuenoClient duenoClient;

    @Autowired
    private VeterinarioClient veterinarioClient;


    public CitaResponseDTO registrarCita(CitaRequestDTO citaRequestDTO) {
        Cita cita = mapToEntity(citaRequestDTO);
        cita.setEstadoCita(EstadoCita.PENDIENTE);  // ← siempre PENDIENTE al crear
        Cita nuevaCita = citaRepository.save(cita);
        return mapToResponseDTO(nuevaCita);
    }


    public List<CitaResponseDTO> obtenerTodas() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }


    public CitaResponseDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el ID: " + id));
        return mapToResponseDTO(cita);
    }


    public CitaResponseDTO actualizarCita(Long id, CitaRequestDTO citaRequestDTO) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el ID: " + id));

        cita.setFechaCita(citaRequestDTO.getFechaCita());
        cita.setMotivoConsulta(citaRequestDTO.getMotivoConsulta());
        cita.setIdMascota(citaRequestDTO.getIdMascota());
        cita.setIdDueno(citaRequestDTO.getIdDueno());
        cita.setIdVeterinario(citaRequestDTO.getIdVeterinario());

        Cita citaActualizada = citaRepository.save(cita);
        return mapToResponseDTO(citaActualizada);
    }


    public CitaResponseDTO actualizarEstado(Long id, ActualizarEstadoDTO dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el ID: " + id));
        cita.setEstadoCita(dto.getEstadoCita());
        Cita citaActualizada = citaRepository.save(cita);
        return mapToResponseDTO(citaActualizada);
    }


    public void eliminarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el ID: " + id));
        citaRepository.delete(cita);
    }

    public List<CitaResponseDTO> obtenerPorMascota(Long idMascota) {
        List<Cita> citas = citaRepository.findByIdMascota(idMascota);
        return citas.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    public List<CitaResponseDTO> obtenerPorFecha(LocalDate fecha) {
        List<Cita> citas = citaRepository.findByFecha(fecha);
        return citas.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    private CitaResponseDTO mapToResponseDTO(Cita cita) {
        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setFechaCita(cita.getFechaCita());
        dto.setMotivoConsulta(cita.getMotivoConsulta());
        dto.setEstadoCita(cita.getEstadoCita());
        dto.setMascota(mascotaClient.obtenerMascota(cita.getIdMascota()));
        dto.setDueno(duenoClient.obtenerDueno(cita.getIdDueno()));
        dto.setVeterinario(veterinarioClient.obtenerVeterinario(cita.getIdVeterinario()));
        return dto;
    }

    private Cita mapToEntity(CitaRequestDTO dto) {
        Cita cita = new Cita();
        cita.setFechaCita(dto.getFechaCita());
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setIdMascota(dto.getIdMascota());
        cita.setIdDueno(dto.getIdDueno());
        cita.setIdVeterinario(dto.getIdVeterinario());
        return cita;
    }
}