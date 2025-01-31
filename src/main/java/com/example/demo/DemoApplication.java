package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.File;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	private Environment env;
	public static void main(String[] args) {

		File envFile = new File(".env");
		if (!envFile.exists()) {
			System.err.println(".env File must be exists");
			System.exit(1);
		}

		Dotenv dotenv = Dotenv.load();

		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Driver loaded successfully!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
		System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));

		SpringApplication.run(DemoApplication.class, args);
	}

}
