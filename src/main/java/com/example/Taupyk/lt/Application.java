package com.example.Taupyk.lt;

import com.example.Taupyk.lt.Repositories.ProductRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private ProductRepositry productRepositry;

	int k = 0;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Count" + productRepositry.count());
	}
}
