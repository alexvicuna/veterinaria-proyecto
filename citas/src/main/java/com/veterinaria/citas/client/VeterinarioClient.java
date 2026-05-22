package com.veterinaria.citas.client;

import com.veterinaria.citas.dto.VeterinarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "veterinarios", url = "${servicios.veterinarios.url}")
public interface VeterinarioClient {

    @GetMapping("/api/v1/veterinarios/{id}")
    VeterinarioDTO obtenerVeterinario(@PathVariable Long id);
}