package com.veterinaria.mascotas.repository;

import com.veterinaria.mascotas.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository

public interface MascotaRepository extends JpaRepository<Mascota, Long>  {



}
