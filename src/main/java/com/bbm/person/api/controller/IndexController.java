package com.bbm.person.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bbm.person.api.model.Usuario;
import com.bbm.person.api.repository.UsuarioRepository;

@CrossOrigin
@RestController
@RequestMapping("/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PostMapping(value = "/", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Usuario> saveUser(@RequestBody Usuario usuario) {

		for (int i = 0; i < usuario.getEnderecos().size(); i++) {
			usuario.getEnderecos().get(i).setUsuario(usuario);
		}

		String senhaCript = new BCryptPasswordEncoder().encode(usuario.getPassWord());
		usuario.setPassWord(senhaCript);
		Usuario user = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(user, HttpStatus.CREATED);
	}

	@GetMapping(value = "/", produces = "application/json")
	@Cacheable("cacheUsuarios")
	public ResponseEntity<List<Usuario>> findAll() throws InterruptedException {

		List<Usuario> usuarios = usuarioRepository.findAll();

		Thread.sleep(6000);
		
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> findById(@PathVariable("id") Long id) {

		Usuario usuario = usuarioRepository.findById(id).get();

		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}

	@PutMapping(value = "/", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> updateUser(@RequestBody Usuario usuario) {

		if (usuario.getId() == null) {

			return new ResponseEntity<String>("Usuário não encontrado para edicão", HttpStatus.OK);

		} else {

			for (int i = 0; i < usuario.getEnderecos().size(); i++) {
				usuario.getEnderecos().get(i).setUsuario(usuario);
			}

			Usuario userTemp = usuarioRepository.findByUserName(usuario.getUsername());
			
			//Caso a senha inserida senha nova ira criptografar para atualizar
			if (!userTemp.getPassWord().equals(usuario.getPassWord())) {
				String senhaCript = new BCryptPasswordEncoder().encode(usuario.getPassWord());
				usuario.setPassWord(senhaCript);
			}

			Usuario user = usuarioRepository.saveAndFlush(usuario);

			return new ResponseEntity<Usuario>(user, HttpStatus.OK);
		}

	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {

		usuarioRepository.deleteById(id);

		return new ResponseEntity<String>("Usuário Deletado com Sucesso", HttpStatus.OK);

	}
}
