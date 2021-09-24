package com.bbm.person.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbm.person.api.model.Usuario;
import com.bbm.person.api.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> saveUser(@RequestBody Usuario usuario) {

		Usuario user = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(user, HttpStatus.CREATED);
	}

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> findAll() {

		List<Usuario> usuarios = usuarioRepository.findAll();

		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> findById(@PathVariable("id") Long id) {

		Usuario usuario = usuarioRepository.findById(id).get();

		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
}
