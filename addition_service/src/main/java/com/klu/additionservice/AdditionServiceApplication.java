package com.klu.additionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdditionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdditionServiceApplication.class, args);
		System.out.println("Application has been running...");
	}

}
