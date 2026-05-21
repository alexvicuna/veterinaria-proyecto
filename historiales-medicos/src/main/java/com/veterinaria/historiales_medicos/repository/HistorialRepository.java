package com.veterinaria.historiales_medicos.repository;

import com.veterinaria.historiales_medicos.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {
}