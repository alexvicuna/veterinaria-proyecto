package com.veterinaria.historiales_medicos.repository;

import com.veterinaria.historiales_medicos.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {

    List<Historial> findByIdMascota(Long idMascota);

    List<Historial> findByIdMascotaAndIdVeterinario(Long idMascota, Long idVeterinario);

    List<Historial> findByFechaAtencionBetween(LocalDate inicio, LocalDate fin);

    List<Historial> findByIdVeterinario(Long idVeterinario);
}