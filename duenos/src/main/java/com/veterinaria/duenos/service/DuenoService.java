package com.veterinaria.duenos.service;

import com.veterinaria.duenos.dto.DuenoRequestDTO;
import com.veterinaria.duenos.dto.DuenoResponseDTO;
import com.veterinaria.duenos.model.Dueno;
import com.veterinaria.duenos.repository.DuenoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DuenoService {
    @Autowired
    private DuenoRepository duenoRepository;

    public DuenoResponseDTO registrarDueno(DuenoRequestDTO duenoDto) {
        Dueno dueno = new Dueno();
        dueno.setNombre(duenoDto.getNombre());
        dueno.setApellido(duenoDto.getApellido());
        dueno.setTelefono(duenoDto.getTelefono());
        dueno.setCorreo(duenoDto.getCorreo());

        return mappearADto(duenoRepository.save(dueno));
    }

    // lista
    public List<DuenoResponseDTO> obtenerTodos() {
        return duenoRepository.findAll().stream()
                .map(this::mappearADto)
                .collect(Collectors.toList());
    }

    // buscar x id
    public DuenoResponseDTO obtenerPorId(Long id) {
        Dueno encontrado = duenoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado con el ID: " + id));
        return mappearADto(encontrado);
    }


    public DuenoResponseDTO actualizarDueno(Long id, DuenoRequestDTO duenoDto) {
        Dueno existente = duenoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado con el ID: " + id));

        existente.setNombre(duenoDto.getNombre());
        existente.setApellido(duenoDto.getApellido());
        existente.setTelefono(duenoDto.getTelefono());
        existente.setCorreo(duenoDto.getCorreo());

        return mappearADto(duenoRepository.save(existente));
    }

    // eliminar
    public void eliminarDueno(Long id) {
        Dueno existente = duenoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado con el ID: " + id));
        duenoRepository.delete(existente);
    }


    // de entidad a DTO
    private DuenoResponseDTO mappearADto(Dueno entidad) {
        DuenoResponseDTO dto = new DuenoResponseDTO();
        dto.setIdDueno(entidad.getIdDueno()); // ← ahora sí incluye el ID
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setTelefono(entidad.getTelefono());
        dto.setCorreo(entidad.getCorreo());
        return dto;
    }
}