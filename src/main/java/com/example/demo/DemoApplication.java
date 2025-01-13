package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Driver loaded successfully!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
