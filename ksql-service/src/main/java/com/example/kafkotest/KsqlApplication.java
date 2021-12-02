package com.example.kafkotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KsqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(KsqlApplication.class, args);
	}

}
