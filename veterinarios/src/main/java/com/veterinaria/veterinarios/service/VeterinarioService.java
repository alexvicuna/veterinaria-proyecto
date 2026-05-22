package com.veterinaria.veterinarios.service; // <-- Tu paquete oficial con 's'

<<<<<<< HEAD

import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
=======
import com.veterinaria.veterinarios.dto.VeterinarioResponseDTO;
>>>>>>> 0429cfed3641891bf219397071c83ecf49cf9344
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

<<<<<<< HEAD

    public VeterinarioRenponseDTO guardarVeterinario(VeterinarioRequestDTO requestDto) {

        Veterinario veterinario = new Veterinario();
        veterinario.setNombreVet(requestDto.getNombreVet());
        veterinario.setEspecialidad(requestDto.getEspecialidad());
        veterinario.setTelefono(requestDto.getTelefono());

        Veterinario guardado = veterinarioRepository.save(veterinario);

        return mapToResponseDTO(guardado);
    }


    public List<VeterinarioRenponseDTO> obtenerTodos() {

        return veterinarioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    private VeterinarioRenponseDTO mapToResponseDTO(Veterinario v) {
        VeterinarioRenponseDTO responseDto = new VeterinarioRenponseDTO();
        responseDto.setIdVeterinario(v.getIdVeterinario());
        responseDto.setNombreVet(v.getNombreVet());
        responseDto.setEspecialidad(v.getEspecialidad());
        responseDto.setTelefono(v.getTelefono());
        return responseDto;
=======
    public VeterinarioResponseDTO registrarVeterinario(VeterinarioRequestDTO dto) {
        Veterinario veterinario = new Veterinario();
        veterinario.setRutVet(dto.getRutVet());
        veterinario.setNombreVet(dto.getNombreVet());
        veterinario.setApellidoVet(dto.getApellidoVet());
        veterinario.setEspecialidad(dto.getEspecialidad());
        veterinario.setTelefono(dto.getTelefono());
        veterinario.setCorreo(dto.getCorreo());
        return mappearaDTO(veterinarioRepository.save(veterinario));
    }

    public List<VeterinarioResponseDTO> obtenerTodos() {
        return veterinarioRepository.findAll().stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public VeterinarioResponseDTO obtenerPorId(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));
        return mappearaDTO(veterinario);
    }

    public VeterinarioResponseDTO obtenerPorRut(String rut) {
        Veterinario veterinario = veterinarioRepository.findByRutVet(rut)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el RUT: " + rut));
        return mappearaDTO(veterinario);
    }

    public List<VeterinarioResponseDTO> obtenerPorEspecialidad(String especialidad) {
        return veterinarioRepository.findByEspecialidadContainingIgnoreCase(especialidad)
                .stream()
                .map(this::mappearaDTO)
                .collect(Collectors.toList());
    }

    public VeterinarioResponseDTO actualizarVeterinario(Long id, VeterinarioRequestDTO dto) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));

        veterinario.setRutVet(dto.getRutVet());
        veterinario.setNombreVet(dto.getNombreVet());
        veterinario.setApellidoVet(dto.getApellidoVet());
        veterinario.setEspecialidad(dto.getEspecialidad());
        veterinario.setTelefono(dto.getTelefono());
        veterinario.setCorreo(dto.getCorreo());

        return mappearaDTO(veterinarioRepository.save(veterinario));
    }

    public void eliminarVeterinario(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con el ID: " + id));
        veterinarioRepository.delete(veterinario);
    }

    private VeterinarioResponseDTO mappearaDTO(Veterinario veterinario) {
        VeterinarioResponseDTO dto = new VeterinarioResponseDTO();
        dto.setIdVeterinario(veterinario.getIdVeterinario());
        dto.setRutVet(veterinario.getRutVet());
        dto.setNombreVet(veterinario.getNombreVet());
        dto.setApellidoVet(veterinario.getApellidoVet());
        dto.setEspecialidad(veterinario.getEspecialidad());
        dto.setTelefono(veterinario.getTelefono());
        dto.setCorreo(veterinario.getCorreo());
        return dto;
>>>>>>> 0429cfed3641891bf219397071c83ecf49cf9344
    }
}