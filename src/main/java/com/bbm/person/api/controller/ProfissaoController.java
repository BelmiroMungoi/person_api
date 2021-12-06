package com.bbm.person.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbm.person.api.model.Profissao;
import com.bbm.person.api.repository.ProfissaoRepository;

@RestController
@RequestMapping(value = "/profissao")
public class ProfissaoController {

	@Autowired
	private ProfissaoRepository profissaoRepository;

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Profissao>> listarProfissao() {
		
		List<Profissao> lista = profissaoRepository.findAll(Sort.by("descricao"));
		
		return new ResponseEntity<List<Profissao>>(lista, HttpStatus.OK);
	}

}
