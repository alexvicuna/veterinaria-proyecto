package com.veterinaria.veterinarios.service;

import com.veterinaria.veterinarios.dto.VeterinarioDTO;
import com.veterinaria.veterinarios.model.VeterinarioModel;
import com.veterinaria.veterinarios.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeterinarioService {

    @Autowired
    private VeterinarioRepository veterinarioRepository;


    public VeterinarioDTO crearVeterinario(VeterinarioDTO dto) {
        VeterinarioModel veterinario = mapToEntity(dto);
        return mapToDTO(veterinarioRepository.save(veterinario));
    }


    public List<VeterinarioDTO> obtenerTodos() {
        return veterinarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    public VeterinarioDTO obtenerPorId(Long id) {
        VeterinarioModel veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));
        return mapToDTO(veterinario);
    }


    public VeterinarioDTO actualizarVeterinario(Long id, VeterinarioDTO dto) {
        VeterinarioModel veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));

        veterinario.setNombreVet(dto.getNombreVet());
        veterinario.setEspecialidad(dto.getEspecialidad());
        veterinario.setTelefono(dto.getTelefono());

        return mapToDTO(veterinarioRepository.save(veterinario));
    }


    public void eliminarVeterinario(Long id) {
        VeterinarioModel veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));
        veterinarioRepository.delete(veterinario);
    }


    private VeterinarioDTO mapToDTO(VeterinarioModel veterinario) {
        VeterinarioDTO dto = new VeterinarioDTO();
        dto.setIdVeterinario(veterinario.getIdVeterinario());
        dto.setNombreVet(veterinario.getNombreVet());
        dto.setEspecialidad(veterinario.getEspecialidad());
        dto.setTelefono(veterinario.getTelefono());
        return dto;
    }


    private VeterinarioModel mapToEntity(VeterinarioDTO dto) {
        VeterinarioModel veterinario = new VeterinarioModel();
        veterinario.setIdVeterinario(dto.getIdVeterinario());
        veterinario.setNombreVet(dto.getNombreVet());
        veterinario.setEspecialidad(dto.getEspecialidad());
        veterinario.setTelefono(dto.getTelefono());
        return veterinario;
    }
}