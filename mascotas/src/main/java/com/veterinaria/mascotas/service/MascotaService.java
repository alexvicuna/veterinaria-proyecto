package com.veterinaria.mascotas.service;

import com.veterinaria.mascotas.client.DuenoClient;
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
    @Autowired
    private DuenoClient duenoClient;

    // registrar mascota con Feign
    public MascotaDTO registrarMascota(MascotaDTO mascotaDto) {
        try {
            Object duenoExistente = duenoClient.obtenerDuenoPorId(mascotaDto.getIdDueno());

            Mascota mascota = new Mascota();
            mascota.setNombreMasc(mascotaDto.getNombreMasc());
            mascota.setEspecie(mascotaDto.getEspecie());
            mascota.setRaza(mascotaDto.getRaza());
            mascota.setEdad(mascotaDto.getEdad());
            mascota.setIdDueno(mascotaDto.getIdDueno());

            Mascota mascotaGuardada = repositoryMascota.save(mascota);

            MascotaDTO respuestaDto = new MascotaDTO();

            respuestaDto.setIdMascota(mascotaGuardada.getIdMascota());
            respuestaDto.setNombreMasc(mascotaGuardada.getNombreMasc());
            respuestaDto.setEspecie(mascotaGuardada.getEspecie());
            respuestaDto.setRaza(mascotaGuardada.getRaza());
            respuestaDto.setEdad(mascotaGuardada.getEdad());
            respuestaDto.setIdDueno(mascotaGuardada.getIdDueno());
            respuestaDto.setNombreDueno(duenoExistente);

            return respuestaDto;

        } catch (Exception e) {
            throw new RuntimeException("No se puede registrar la mascota: El Dueño con el ID " +
                    mascotaDto.getIdDueno() + " no existe.");
        }
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
    private MascotaDTO mappearADto(Mascota mascota) {
        MascotaDTO dto = new MascotaDTO();

        dto.setIdMascota(mascota.getIdMascota());
        dto.setNombreMasc(mascota.getNombreMasc());
        dto.setEspecie(mascota.getEspecie());
        dto.setRaza(mascota.getRaza());
        dto.setEdad(mascota.getEdad());
        dto.setIdDueno(mascota.getIdDueno());

        try {
            Object dueno = duenoClient.obtenerDuenoPorId(mascota.getIdDueno());
            dto.setNombreDueno(dueno);
        } catch (Exception e) {
            dto.setNombreDueno("Dueño no encontrado");
        }

        return dto;
    }
}


