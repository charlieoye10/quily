package com.example.quily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@ComponentScan({"com.example.quily.DAO",
		"com.example.quily.services",
		"com.example.quily.handler",
		"com.example.quily.router"})
@EntityScan("com.example.quily.model")
@EnableR2dbcRepositories("com.example.quily.repositories")
public class QuilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuilyApplication.class, args);
	}
	
}
