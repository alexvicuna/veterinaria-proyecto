package com.veterinaria.citas.client;

import com.veterinaria.citas.dto.DuenoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "duenos", url = "${servicios.duenos.url}")
public interface DuenoClient {

    @GetMapping("/api/v1/duenos/{id}")
    DuenoDTO obtenerDueno(@PathVariable Long id);
}