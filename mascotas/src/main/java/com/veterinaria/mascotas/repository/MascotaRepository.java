package com.veterinaria.mascotas.repository;

import com.veterinaria.mascotas.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface MascotaRepository extends JpaRepository<Mascota, Long>  {
    List<Mascota> findByNombreMascContainingIgnoreCase(String nombre);
    List<Mascota> findByRazaContainingIgnoreCase(String raza);
    List<Mascota> findByEspecieContainingIgnoreCase(String especie);

    List<Mascota> findByIdDueno(Long idDueno);
}
