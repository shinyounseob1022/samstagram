package com.sparta.samstagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SamstagramApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamstagramApplication.class, args);
	}

}
