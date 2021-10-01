package com.bbm.person.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

//Filter onde todas as requisicoes serao capturadas para autenticacao
public class JwtApiAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		//Estabelece a autenticacao para a requisacao
		Authentication authentication = new JwtAuthenticationService()
				.getAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
		
		//Coloca o processo de autenticacao no spring security
		SecurityContextHolder.getContext().setAuthentication(authentication);
	
		//Continua o processo
		chain.doFilter(request, response);
	}

}
