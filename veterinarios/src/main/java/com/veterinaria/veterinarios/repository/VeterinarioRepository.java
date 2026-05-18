package com.veterinaria.veterinarios.repository;


import com.veterinaria.veterinarios.model.VeterinarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinarioRepository extends JpaRepository<VeterinarioModel, Long> {
}