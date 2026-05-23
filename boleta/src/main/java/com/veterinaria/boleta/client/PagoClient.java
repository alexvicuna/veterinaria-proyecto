package com.veterinaria.boleta.client;

import com.veterinaria.boleta.dto.PagoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pagos", url = "${servicios.pagos.url}")
public interface PagoClient {

    @GetMapping("/api/v1/pagos/{id}")
    PagoDTO obtenerPago(@PathVariable Long id);
}