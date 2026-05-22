package com.veterinaria.boleta.repository;

import com.veterinaria.boleta.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    List<Boleta> findByFecha(LocalDate fecha);

    List<Boleta> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}