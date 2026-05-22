package com.veterinaria.veterinarios.repository;

import com.veterinaria.veterinarios.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
<<<<<<< HEAD

=======
    Optional<Veterinario> findByRutVet(String rutVet);
    List<Veterinario> findByEspecialidadContainingIgnoreCase(String especialidad);
>>>>>>> 0429cfed3641891bf219397071c83ecf49cf9344
}