package com.veterinaria.veterinarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VeterinariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeterinariosApplication.class, args);
	}

}
