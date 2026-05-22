package com.veterinaria.duenos.repository;

import com.veterinaria.duenos.model.Dueno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DuenoRepository extends JpaRepository<Dueno, Long> {
    Optional<Dueno> findByRut(String rut);
}
