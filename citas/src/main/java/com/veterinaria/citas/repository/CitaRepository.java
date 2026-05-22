package com.veterinaria.citas.repository;

import com.veterinaria.citas.model.Cita;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByIdMascota(Long idMascota);
    @Query("SELECT c FROM Cita c WHERE DATE(c.fechaCita) = :fecha")
    List<Cita> findByFecha(@Param("fecha") LocalDate fecha);
}