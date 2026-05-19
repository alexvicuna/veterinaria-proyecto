package com.veterinaria.mascotas.client;

import com.veterinaria.mascotas.dto.DuenoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-duenos", url = "http://localhost:8080/api/v1/duenos")
public interface DuenoClient {

    @GetMapping("/{id}")
    DuenoDTO obtenerDuenoPorId(@PathVariable("id") Long id);
}