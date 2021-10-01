package com.bbm.person.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "com.bbm.person.api.model")
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories(basePackages = "com.bbm.person.api.repository")
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@EnableCaching
public class PersonApiApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(PersonApiApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("admin"));
	}
	
	//Mapeamento global
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		//Libera o acesso aos End-Points da API
		registry.addMapping("/**")
		.allowedMethods("*")// libera quais metodos https(GET, POST, etc) podem ser acessados
		.allowedOrigins("*");// libera o acesso para requisicoes de outros servers
	}

}
