package com.veterinaria.boleta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BoletaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoletaApplication.class, args);
    }
}