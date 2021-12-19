package com.ocko.aventador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AventadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AventadorApplication.class, args);
	}

}

