package com.veterinaria.duenos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DuenosApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuenosApplication.class, args);
	}

}
