package com.veterinaria.recetas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RecetasApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecetasApplication.class, args);
	}

}
