package com.veterinaria.veterinarios.service;

import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
import com.veterinaria.veterinarios.dto.VeterinarioResponseDTO;
import com.veterinaria.veterinarios.model.Veterinario;
import com.veterinaria.veterinarios.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeterinarioService {

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    // Crear nuevo veterinario
    public VeterinarioResponseDTO guardarVeterinario(VeterinarioRequestDTO requestDto) {
        Veterinario veterinario = new Veterinario();
        veterinario.setNombreVet(requestDto.getNombreVet());
        veterinario.setEspecialidad(requestDto.getEspecialidad());
        veterinario.setTelefono(requestDto.getTelefono());

        Veterinario guardado = veterinarioRepository.save(veterinario);
        return mapToResponseDTO(guardado);
    }

    // Listar todos los veterinarios
    public List<VeterinarioResponseDTO> obtenerTodos() {
        return veterinarioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar por ID
    public VeterinarioResponseDTO obtenerPorId(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));
        return mapToResponseDTO(veterinario);
    }

    // Buscar por RUT
    public VeterinarioResponseDTO obtenerPorRut(String rut) {
        Veterinario veterinario = veterinarioRepository.findByRutVet(rut)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el RUT: " + rut));
        return mapToResponseDTO(veterinario);
    }

    // Buscar por especialidad
    public List<VeterinarioResponseDTO> obtenerPorEspecialidad(String especialidad) {
        return veterinarioRepository.findByEspecialidadContainingIgnoreCase(especialidad)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Actualizar veterinario
    public VeterinarioResponseDTO actualizarVeterinario(Long id, VeterinarioRequestDTO dto) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));

        veterinario.setNombreVet(dto.getNombreVet());
        veterinario.setEspecialidad(dto.getEspecialidad());
        veterinario.setTelefono(dto.getTelefono());

        return mapToResponseDTO(veterinarioRepository.save(veterinario));
    }

    // Eliminar veterinario
    public void eliminarVeterinario(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));
        veterinarioRepository.delete(veterinario);
    }

    // Mapper de entidad a DTO
    private VeterinarioResponseDTO mapToResponseDTO(Veterinario veterinario) {
        VeterinarioResponseDTO dto = new VeterinarioResponseDTO();
        dto.setIdVeterinario(veterinario.getIdVeterinario());
        dto.setRutVet(veterinario.getRutVet());
        dto.setNombreVet(veterinario.getNombreVet());
        dto.setApellidoVet(veterinario.getApellidoVet());
        dto.setEspecialidad(veterinario.getEspecialidad());
        dto.setTelefono(veterinario.getTelefono());
        dto.setCorreo(veterinario.getCorreo());
        return dto;
    }