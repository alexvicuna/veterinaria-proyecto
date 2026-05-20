package com.veterinaria.veterinarios.service; // <-- Tu paquete oficial con 's'


import com.veterinaria.veterinarios.dto.VeterinarioRequestDTO;
import com.veterinaria.veterinarios.dto.VeterioRenponseDTO;
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


    public VeterioRenponseDTO guardarVeterinario(VeterinarioRequestDTO requestDto) {

        Veterinario veterinario = new Veterinario();
        veterinario.setNombreVet(requestDto.getNombreVet());
        veterinario.setEspecialidad(requestDto.getEspecialidad());
        veterinario.setTelefono(requestDto.getTelefono());

        Veterinario guardado = veterinarioRepository.save(veterinario);

        return mapToResponseDTO(guardado);
    }


    public List<VeterioRenponseDTO> obtenerTodos() {

        return veterinarioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    private VeterioRenponseDTO mapToResponseDTO(Veterinario v) {
        VeterioRenponseDTO responseDto = new VeterioRenponseDTO();
        responseDto.setIdVeterinario(v.getIdVeterinario());
        responseDto.setNombreVet(v.getNombreVet());
        responseDto.setEspecialidad(v.getEspecialidad());
        responseDto.setTelefono(v.getTelefono());
        return responseDto;
    }
}