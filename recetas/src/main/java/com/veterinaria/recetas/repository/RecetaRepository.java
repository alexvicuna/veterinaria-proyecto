package com.veterinaria.recetas.repository;

import com.veterinaria.recetas.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {

    List<Receta> findByIdMascota(Long idMascota);

    List<Receta> findByIdVeterinario(Long idVeterinario);

    List<Receta> findByFechaEmisionBetween(LocalDate inicio, LocalDate fin);

    List<Receta> findByIdMascotaAndIdVeterinario(Long idMascota, Long idVeterinario);
}