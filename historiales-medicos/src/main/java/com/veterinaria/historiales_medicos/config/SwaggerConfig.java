package com.veterinaria.historiales_medicos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI historialesMedicosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio - Historiales Médicos")
                        .description("API REST para la gestión de historiales médicos de las mascotas. " +
                                "Permite registrar atenciones, diagnósticos, tratamientos y vacunas, " +
                                "validando que la mascota exista antes de asociarle un historial.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Veterinaria")
                                .email("veterinaria@duoc.cl")));
    }
}