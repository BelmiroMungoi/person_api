package com.bbm.person.api.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbm.person.api.exception.ErrorObject;
import com.bbm.person.api.model.Usuario;
import com.bbm.person.api.repository.UsuarioRepository;
import com.bbm.person.api.service.SendMailService;

@RestController
@RequestMapping(value = "/recuperar")
public class RecuperaController {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private SendMailService mailService;
	
	@PostMapping(value = "/")
	public ResponseEntity<ErrorObject> recuperar(@RequestBody Usuario login) throws Exception {
		
		ErrorObject error = new ErrorObject();
		
		Usuario user = repository.findByUserName(login.getUsername());
		
		if (user == null) {			
			error.setCode("404");
			error.setError("Usuário não encontrado");
		} else {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String senhaNova = dateFormat.format(Calendar.getInstance().getTime());
			
			repository.updateSenha(senhaNova, user.getId());
			
			mailService.sendMail("Recuperacão de Senha",
					user.getUserName(),
					"Sua senha nova é: " + senhaNova);
			
			error.setCode("200");
			error.setError("Acesso enviado para o seu e-mail!");
		}
		
        return new ResponseEntity<ErrorObject>(error, HttpStatus.OK);	
	}
}
