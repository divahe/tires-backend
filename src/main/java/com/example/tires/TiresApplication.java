package com.example.tires;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TiresApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiresApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner() {
//		return args -> {
//			Environment environment;
//			System.out.println(environment.getProperty('mappings'));
//		};
//	}

}
