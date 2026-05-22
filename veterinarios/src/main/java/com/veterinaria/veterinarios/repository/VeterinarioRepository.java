package com.veterinaria.veterinarios.repository;

import com.veterinaria.veterinarios.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    Optional<Veterinario> findByRutVet(String rutVet);
    List<Veterinario> findByEspecialidadContainingIgnoreCase(String especialidad);
}