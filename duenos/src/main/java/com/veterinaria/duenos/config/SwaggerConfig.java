package com.veterinaria.duenos.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI duenosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio - Dueños")
                        .description("API REST para la gestión de dueños de mascotas en el sistema veterinario. " +
                                "Permite registrar, consultar, actualizar y eliminar dueños, " +
                                "así como buscarlos por RUT y ver sus mascotas asociadas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Veterinaria")
                                .email("veterinaria@duoc.cl")));
    }
}
