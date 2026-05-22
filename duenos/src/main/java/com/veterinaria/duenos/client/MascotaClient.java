package com.veterinaria.duenos.client;

import com.veterinaria.duenos.dto.MascotaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "microservicio-mascotas", url = "http://localhost:8082/api/v1/mascotas")
public interface MascotaClient {

    @GetMapping("/dueno/{idDueno}")
    List<MascotaDTO> obtenerMascotasPorDueno(@PathVariable("idDueno") Long idDueno);

}
