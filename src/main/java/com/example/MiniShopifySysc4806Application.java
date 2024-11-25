package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication
@SpringBootApplication(scanBasePackages = "com.example")
public class MiniShopifySysc4806Application {

	public static void main(String[] args) {
		SpringApplication.run(MiniShopifySysc4806Application.class, args);
	}

}
