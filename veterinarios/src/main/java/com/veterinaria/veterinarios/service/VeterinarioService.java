package com.veterinaria.veterinarios.service;

import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO; // <-- Importación agregada
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


    public VeterinarioResponseDTO crearVeterinario(VeterinarioRequestDTO dto) {
        Veterinario veterinario = mapToEntity(dto);
        return mapToDTO(veterinarioRepository.save(veterinario));
    }


    public List<VeterinarioResponseDTO> obtenerTodos() {
        return veterinarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    public VeterinarioResponseDTO obtenerPorId(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));
        return mapToDTO(veterinario);
    }


    public VeterinarioResponseDTO actualizarVeterinario(Long id, VeterinarioRequestDTO dto) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));

        veterinario.setNombreVet(dto.getNombreVet());
        veterinario.setEspecialidad(dto.getEspecialidad());
        veterinario.setTelefono(dto.getTelefono());

        return mapToDTO(veterinarioRepository.save(veterinario));
    }

    public void eliminarVeterinario(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));
        veterinarioRepository.delete(veterinario);
    }


    private VeterinarioResponseDTO mapToDTO(Veterinario veterinario) {
        VeterinarioResponseDTO dto = new VeterinarioResponseDTO();
        dto.setIdVeterinario(veterinario.getIdVeterinario());
        dto.setNombreVet(veterinario.getNombreVet());
        dto.setEspecialidad(veterinario.getEspecialidad());
        dto.setTelefono(veterinario.getTelefono());
        return dto;
    }


    private Veterinario mapToEntity(VeterinarioRequestDTO dto) {
        Veterinario veterinario = new Veterinario();
        veterinario.setNombreVet(dto.getNombreVet());
        veterinario.setEspecialidad(dto.getEspecialidad());
        veterinario.setTelefono(dto.getTelefono());
        return veterinario;
    }
}