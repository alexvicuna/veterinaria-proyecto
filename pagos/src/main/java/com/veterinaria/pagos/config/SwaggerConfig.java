package com.veterinaria.pagos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI pagosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio - Pagos")
                        .description("API REST para el procesamiento de pagos asociados a citas veterinarias. " +
                                "Permite registrar, consultar y actualizar el estado de los pagos " +
                                "(pendiente, completado, rechazado, reembolsado).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Veterinaria")
                                .email("veterinaria@duoc.cl")));
    }
}