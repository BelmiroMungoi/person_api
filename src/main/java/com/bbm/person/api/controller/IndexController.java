package com.bbm.person.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<?> init() {
		return new ResponseEntity<String>("Olá, Testando o Servico Rest", HttpStatus.OK);
	}
}
