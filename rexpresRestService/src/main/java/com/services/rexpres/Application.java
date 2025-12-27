package com.services.rexpres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.services.rexpres",
"com.auth.*"})
//@EntityScan(basePackages={"com.persistence.entity"})
@EnableJpaRepositories(basePackages = {"com.services.rexpres.repository"})
//@Configuration
//@ComponentScan("com.*")
//@EnableAutoConfiguration
//@PropertySource("classpath:com/auth/config/properties/auth.properties")
public class Application extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	}

