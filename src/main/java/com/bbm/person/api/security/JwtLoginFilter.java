package com.bbm.person.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bbm.person.api.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

//Estabelece o gerenciador do Token
public class JwtLoginFilter  extends AbstractAuthenticationProcessingFilter{

	//Configuracao do gerenciador de autenticacao
	protected JwtLoginFilter(String url, AuthenticationManager authenticationManager) {
		
		//Obriga a autenticar a url
		super(new AntPathRequestMatcher(url));
		
		//Gerenciador da autenticacao
		setAuthenticationManager(authenticationManager);
	}

	//Retorna o usuario ao processar a autenticacao
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		//Pega o token para a validacao
		Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
		
		//Retorna o userName do usuario, a senha e os acessos
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		new JwtAuthenticationService().addAuthentication(response, authResult.getName());
	}

}
