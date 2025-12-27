package com.auth.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.security.RexpresSecurityConfig;
import com.security.jwt.JwtTokenFilter;

@Configuration
@EntityScan("com.*")
@PropertySource("classpath:properties/auth.properties")
@EnableJpaRepositories(basePackages = {"com.auth.repositories"})
@Import(value = RexpresSecurityConfig.class)
public class AuthConfig {

	@Bean(name = "jwtTokenRexpresFilter")
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter();
	}
	
	@Bean(name = "passwordEncoder")
	public BCryptPasswordEncoder bcruptPassword() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
