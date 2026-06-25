package com.veterinaria.citas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI citasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio - Citas")
                        .description("API REST para la gestión de citas veterinarias. " +
                                "Permite registrar, consultar, actualizar y cancelar citas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Veterinaria")
                                .email("veterinaria@duoc.cl")));
    }
}