package com.veterinaria.mascotas.service;

import org.springframework.stereotype.Service;
import com.veterinaria.mascotas.dto.MascotaDTO;
import com.veterinaria.mascotas.model.Mascota;
import com.veterinaria.mascotas.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository repositoryMascota;

    // registrar mascota
    public MascotaDTO registrarMascota(MascotaDTO mascotaDto) {
        Mascota mascota = new Mascota();

        mascota.setNombreMasc(mascotaDto.getNombreMasc());
        mascota.setEspecie(mascotaDto.getEspecie());
        mascota.setRaza(mascotaDto.getRaza());
        mascota.setEdad(mascotaDto.getEdad());

        Mascota mascotaGuardada = repositoryMascota.save(mascota);
        return mappearADto(mascotaGuardada);
    }

    // lista de mascotas
    public List<MascotaDTO> obtenerTodas() {
        return repositoryMascota.findAll().stream()
                .map(this::mappearADto)
                .collect(Collectors.toList());
    }

    // buscar mascota id
    public MascotaDTO obtenerPorId(Long id) {
        Mascota encontrada = repositoryMascota.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con el ID: " + id));
        return mappearADto(encontrada);
    }

    // actualizar datos
    public MascotaDTO actualizarMascota(Long id, MascotaDTO mascotaDto) {
        Mascota existente = repositoryMascota.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con el ID: " + id));

        existente.setNombreMasc(mascotaDto.getNombreMasc());
        existente.setEspecie(mascotaDto.getEspecie());
        existente.setRaza(mascotaDto.getRaza());
        existente.setEdad(mascotaDto.getEdad());

        Mascota actualizada = repositoryMascota.save(existente);
        return mappearADto(actualizada);
    }

    // eliminar mascota
    public void eliminarMascota(Long id) {
        Mascota existente = repositoryMascota.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con el ID: " + id));
        repositoryMascota.delete(existente);
    }


    // de entidad a DTO
    private MascotaDTO mappearADto(Mascota entidad) {
        MascotaDTO dto = new MascotaDTO();
        dto.setNombreMasc(entidad.getNombreMasc());
        dto.setEspecie(entidad.getEspecie());
        dto.setRaza(entidad.getRaza());
        dto.setEdad(entidad.getEdad());
        return dto;
    }

}


