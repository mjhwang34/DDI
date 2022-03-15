package com.monorama.ddi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan
public class DdiApplication extends SpringBootServletInitializer {
	@Override 
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 
		return application.sources(DdiApplication.class); 
	}

	public static void main(String[] args) {
		SpringApplication.run(DdiApplication.class, args);
	}

}
