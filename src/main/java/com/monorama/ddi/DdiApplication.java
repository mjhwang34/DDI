package com.monorama.ddi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class DdiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DdiApplication.class, args);
	}

}
