package com.veterinaria.veterinarios.service;

import com.veterinaria.veterinarios.dto.VeterinarioDTO;
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


    public VeterinarioDTO crearVeterinario(VeterinarioDTO dto) {
        Veterinario veterinario = mapToEntity(dto);
        return mapToDTO(veterinarioRepository.save(veterinario));
    }


    public List<VeterinarioDTO> obtenerTodos() {
        return veterinarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    public VeterinarioDTO obtenerPorId(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));
        return mapToDTO(veterinario);
    }


    public VeterinarioDTO actualizarVeterinario(Long id, VeterinarioDTO dto) {
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


    private VeterinarioDTO mapToDTO(Veterinario veterinario) {
        VeterinarioDTO dto = new VeterinarioDTO();
        dto.setIdVeterinario(veterinario.getIdVeterinario());
        dto.setNombreVet(veterinario.getNombreVet());
        dto.setEspecialidad(veterinario.getEspecialidad());
        dto.setTelefono(veterinario.getTelefono());
        return dto;
    }


    private Veterinario mapToEntity(VeterinarioDTO dto) {
        Veterinario veterinario = new Veterinario();
        veterinario.setIdVeterinario(dto.getIdVeterinario());
        veterinario.setNombreVet(dto.getNombreVet());
        veterinario.setEspecialidad(dto.getEspecialidad());
        veterinario.setTelefono(dto.getTelefono());
        return veterinario;
    }
}