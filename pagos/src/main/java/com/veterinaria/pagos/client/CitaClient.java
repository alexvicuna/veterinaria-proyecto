package com.veterinaria.pagos.client;

import com.veterinaria.pagos.dto.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "citas", url = "${servicios.citas.url}")
public interface CitaClient {

    @GetMapping("/api/v1/citas/{id}")
    CitaDTO obtenerCita(@PathVariable Long id);
}