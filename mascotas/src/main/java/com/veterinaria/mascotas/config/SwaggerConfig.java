package com.veterinaria.mascotas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mascotasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio - Mascotas")
                        .description("API REST para la gestión de mascotas en el sistema veterinario. " +
                                "Permite crear, consultar, actualizar y eliminar mascotas, " +
                                "así como buscarlas por nombre, raza, especie o dueño.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Veterinaria")
                                .email("veterinaria@duoc.cl")));
    }
}
