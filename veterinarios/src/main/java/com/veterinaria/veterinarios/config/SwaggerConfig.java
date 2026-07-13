package com.veterinaria.veterinarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI veterinariosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio - Veterinarios")
                        .description("API REST para la gestión de veterinarios en el sistema. " +
                                "Permite registrar, consultar, actualizar y eliminar veterinarios, " +
                                "así como buscarlos por RUT o especialidad.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Veterinaria")
                                .email("veterinaria@duoc.cl")));
    }
}