package com.veterinaria.recetas.client;

import com.veterinaria.recetas.dto.MascotaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mascotas", url = "${servicios.mascotas.url}")
public interface MascotaClient {
    @GetMapping("/api/v1/mascotas/{id}")
    MascotaDTO obtenerMascota(@PathVariable Long id);
}

