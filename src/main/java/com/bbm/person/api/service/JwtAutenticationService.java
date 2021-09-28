package com.bbm.person.api.service;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JwtAutenticationService {

	//Tempo de validade do Token(2 dias)
	private static final long EXPIRATION_TIME = 172800000;
	
	//Uma senha unica para compor a autenticacao e ajudar na seguranca
	private static final String SECRET = "SenhaSuperMegaSecreta";
	
	//Prefixo padrao do Token
	private static final String TOKEN_PREFIX = "Bearer";
	
	//Prefixo a ser retornado ao Token
	private static final String HEADER_STRING = "Authorization";
	
	//Gera Token de autenticacao e adiciona ao cabecalho e respoata Http
	public void addAuthentication(HttpServletResponse response, String userName) throws Exception{
		
		//Montagem do Token
		String jwt = Jwts
				.builder().setSubject(userName)//Adiciona o usuario
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))//Adiciona o tempo de expircao
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();// compactacao e algoritmos de geracao de senha
	
		//Junta o token com o prefixo
		String token = TOKEN_PREFIX + " " + jwt;
		
		//Adiciona no cabecalho http
		response.addHeader(HEADER_STRING, token);
		
		//Escreve o token como resposta no corpo http
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
}
