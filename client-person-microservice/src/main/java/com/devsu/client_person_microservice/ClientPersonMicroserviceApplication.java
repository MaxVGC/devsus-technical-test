package com.devsu.client_person_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {ReactiveUserDetailsServiceAutoConfiguration.class})
public class ClientPersonMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientPersonMicroserviceApplication.class, args);
	}

}
