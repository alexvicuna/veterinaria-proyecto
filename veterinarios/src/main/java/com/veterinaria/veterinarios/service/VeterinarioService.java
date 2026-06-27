package com.veterinaria.veterinarios.service;

import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
import com.veterinaria.veterinarios.dto.VeterinarioResponseDTO;
import com.veterinaria.veterinarios.model.Veterinario;
import com.veterinaria.veterinarios.repository.VeterinarioRepository;
import com.veterinaria.veterinarios.veterinariosException.VeterinariosNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeterinarioService {

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    public VeterinarioResponseDTO guardarVeterinario(VeterinarioRequestDTO requestDto) {
        if (veterinarioRepository.findByRutVet(requestDto.getRutVet()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un veterinario registrado con el RUT: " + requestDto.getRutVet());
        }

        Veterinario veterinario = new Veterinario();
        veterinario.setRutVet(requestDto.getRutVet());
        veterinario.setNombreVet(requestDto.getNombreVet());
        veterinario.setApellidoVet(requestDto.getApellidoVet());
        veterinario.setEspecialidad(requestDto.getEspecialidad());
        veterinario.setTelefono(requestDto.getTelefono());
        veterinario.setCorreo(requestDto.getCorreo());
        return mapToResponseDTO(veterinarioRepository.save(veterinario));
    }

    public List<VeterinarioResponseDTO> obtenerTodos() {
        return veterinarioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public VeterinarioResponseDTO obtenerPorId(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new VeterinariosNotFoundException(id));
        return mapToResponseDTO(veterinario);
    }

    public VeterinarioResponseDTO obtenerPorRut(String rut) {
        Veterinario veterinario = veterinarioRepository.findByRutVet(rut)
                .orElseThrow(() -> new VeterinariosNotFoundException(rut));
        return mapToResponseDTO(veterinario);
    }

    public List<VeterinarioResponseDTO> obtenerPorEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.isBlank()) {
            throw new IllegalArgumentException("La especialidad no puede estar vacia para realizar la busqueda");
        }
        return veterinarioRepository.findByEspecialidadContainingIgnoreCase(especialidad)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public VeterinarioResponseDTO actualizarVeterinario(Long id, VeterinarioRequestDTO dto) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new VeterinariosNotFoundException(id));

        if (!veterinario.getRutVet().equals(dto.getRutVet())) {
            if (veterinarioRepository.findByRutVet(dto.getRutVet()).isPresent()) {
                throw new IllegalArgumentException(
                        "Ya existe un veterinario registrado con el RUT: " + dto.getRutVet());
            }
        }

        veterinario.setRutVet(dto.getRutVet());
        veterinario.setNombreVet(dto.getNombreVet());
        veterinario.setApellidoVet(dto.getApellidoVet());
        veterinario.setEspecialidad(dto.getEspecialidad());
        veterinario.setTelefono(dto.getTelefono());
        veterinario.setCorreo(dto.getCorreo());

        return mapToResponseDTO(veterinarioRepository.save(veterinario));
    }

    public void eliminarVeterinario(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new VeterinariosNotFoundException(id));
        veterinarioRepository.delete(veterinario);
    }

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
}