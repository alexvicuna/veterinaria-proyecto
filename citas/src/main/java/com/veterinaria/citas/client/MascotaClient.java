package com.veterinaria.citas.client;

import com.veterinaria.citas.dto.MascotaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mascotas", url = "${servicios.mascotas.url}")
public interface MascotaClient {

    @GetMapping("/mascotas/{id}")
    MascotaDTO obtenerMascota(@PathVariable Long id);
}