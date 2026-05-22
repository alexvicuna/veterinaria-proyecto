package com.veterinaria.mascotas.service;

import com.veterinaria.mascotas.client.DuenoClient;
import com.veterinaria.mascotas.dto.DuenoDTO;
import com.veterinaria.mascotas.dto.MascotaResponseDTO;
import com.veterinaria.mascotas.mascotaException.MascotaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.veterinaria.mascotas.dto.MascotaRequestDTO;
import com.veterinaria.mascotas.model.Mascota;
import com.veterinaria.mascotas.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.veterinaria.mascotas.dto.MascotaDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;
    @Autowired
    private DuenoClient duenoClient;

    private MascotaResponseDTO toResponseDTO(Mascota mascota) {
        MascotaResponseDTO dto = new MascotaResponseDTO();
        dto.setIdMascota(mascota.getIdMascota());
        dto.setNombreMasc(mascota.getNombreMasc());
        dto.setEspecie(mascota.getEspecie());
        dto.setRaza(mascota.getRaza());
        dto.setEdad(mascota.getEdad());

        DuenoDTO dueno = duenoClient.obtenerDuenoPorId(mascota.getIdDueno());
        dto.setDueno(dueno);

        return dto;
    }

    public MascotaResponseDTO crearMascota(MascotaRequestDTO request) {
        Mascota mascota = new Mascota();
        mascota.setNombreMasc(request.getNombreMasc());
        mascota.setEspecie(request.getEspecie());
        mascota.setRaza(request.getRaza());
        mascota.setEdad(request.getEdad());
        mascota.setIdDueno(request.getIdDueno());

        return toResponseDTO(mascotaRepository.save(mascota));
    }

    public List<MascotaResponseDTO> obtenerTodas() {
        return mascotaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MascotaResponseDTO obtenerPorId(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNotFoundException(id));
        return toResponseDTO(mascota);
    }

    public MascotaResponseDTO actualizar(Long id, MascotaRequestDTO request) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new MascotaNotFoundException(id));

        mascota.setNombreMasc(request.getNombreMasc());
        mascota.setEspecie(request.getEspecie());
        mascota.setRaza(request.getRaza());
        mascota.setEdad(request.getEdad());
        mascota.setIdDueno(request.getIdDueno());

        return toResponseDTO(mascotaRepository.save(mascota));
    }

    public void eliminar(Long id) {
        if (!mascotaRepository.existsById(id)) {
            throw new MascotaNotFoundException(id);
        }
        mascotaRepository.deleteById(id);
    }

    public List<MascotaResponseDTO> buscarPorNombre(String nombre) {
        return mascotaRepository.findByNombreMascContainingIgnoreCase(nombre)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MascotaResponseDTO> buscarPorRaza(String raza) {
        return mascotaRepository.findByRazaContainingIgnoreCase(raza)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MascotaResponseDTO> buscarPorEspecie(String especie) {
        return mascotaRepository.findByEspecieContainingIgnoreCase(especie)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MascotaDTO> buscarPorDueno(Long idDueno) {
        return mascotaRepository.findByIdDueno(idDueno)
                .stream()
                .map(this::toMascotaDTO) // ← mapper simple sin llamar a dueños
                .collect(Collectors.toList());
    }

    private MascotaDTO toMascotaDTO(Mascota mascota) {
        MascotaDTO dto = new MascotaDTO();
        dto.setIdMascota(mascota.getIdMascota());
        dto.setNombreMasc(mascota.getNombreMasc());
        dto.setEspecie(mascota.getEspecie());
        dto.setRaza(mascota.getRaza());
        dto.setEdad(mascota.getEdad());
        return dto;
    }

}
