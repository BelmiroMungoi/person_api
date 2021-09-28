package com.bbm.person.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bbm.person.api.service.ImplementationUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private ImplementationUserDetailsService detailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()//protege contra usuarios nao validados por token 
		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.disable().authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/login").permitAll()// permite o acesso a pagina de login
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/login")//redeciona o usuario quando fizer logout
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//invalida a sessao do usuario
		;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailsService)// consulta usuario no banco de dados
		.passwordEncoder(new BCryptPasswordEncoder()); //codificacao da senha do usuario
	}
}
