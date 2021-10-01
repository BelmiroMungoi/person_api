package com.bbm.person.api.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.bbm.person.api.ApplicationContextLoad;
import com.bbm.person.api.model.Usuario;
import com.bbm.person.api.repository.UsuarioRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JwtAuthenticationService {

	// Tempo de validade do Token(2 dias)
	private static final long EXPIRATION_TIME = 172800000;

	// Uma senha unica para compor a autenticacao e ajudar na seguranca
	private static final String SECRET = "SenhaSuperMegaSecreta";

	// Prefixo padrao do Token
	private static final String TOKEN_PREFIX = "Bearer";

	// Prefixo a ser retornado ao Token
	private static final String HEADER_STRING = "Authorization";

	// Gera Token de autenticacao e adiciona ao cabecalho e respoata Http
	public void addAuthentication(HttpServletResponse response, String userName) throws IOException {

		// Montagem do Token
		String jwt = Jwts.builder().setSubject(userName)// Adiciona o usuario
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))// Adiciona o tempo de expircao
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();// compactacao e algoritmos de geracao de senha

		// Junta o token com o prefixo
		String token = TOKEN_PREFIX + " " + jwt;

		// Adiciona no cabecalho http
		response.addHeader(HEADER_STRING, token);

		//Atualiza o token na base de dados
		ApplicationContextLoad.getContext().getBean(UsuarioRepository.class).updateTokenUser(jwt, userName);
		
		// liberando a resposta para outros servers
		liberaCors(response);

		// Escreve o token como resposta no corpo http
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}

	// Retorna o usuario validado, caso nao retorna null
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

		// Pega o token enviado no cabelhaco
		String token = request.getHeader(HEADER_STRING);

		try {
			if (token != null) {

				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

				// Faz a validacao do token do usuario na requisacao
				String user = Jwts.parser().setSigningKey(SECRET)// Pega o token
						.parseClaimsJws(tokenLimpo)// Retira o prefixo do token
						.getBody().getSubject();// Retorna o usuario

				if (user != null) {

					Usuario usuario = ApplicationContextLoad.getContext().getBean(UsuarioRepository.class)
							.findByUserName(user);

					if (usuario != null) {
						// verifica se o token passando é igual ao token na base de dados
						if (tokenLimpo.equalsIgnoreCase(usuario.getToken())) {
							return new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword(),
									usuario.getAuthorities());
						}
					}
				}
			}
		} catch (ExpiredJwtException e) {
			try {
				response.getOutputStream().println("Token Expirado");
			} catch (IOException e1) {

			}
		}

		liberaCors(response);
		return null;

	}

	private void liberaCors(HttpServletResponse response) {

		if (response.getHeader("Acess-Control-Allow-Origin") == null) {
			response.addHeader("Acess-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Acess-Control-Allow-Headers") == null) {
			response.addHeader("Acess-Control-Allow-Headers", "*");
		}

		if (response.getHeader("Acess-Control-Request-Headers") == null) {
			response.addHeader("Acess-Control-Request-Headers", "*");
		}

		if (response.getHeader("Acess-Control-Allow-Methods") == null) {
			response.addHeader("Acess-Control-Allow-Methods", "*");
		}
	}
}
