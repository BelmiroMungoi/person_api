package com.bbm.person.api.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.map.HashedMap;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.bbm.person.api.model.UserReport;
import com.bbm.person.api.model.Usuario;
import com.bbm.person.api.repository.EnderecoRepository;
import com.bbm.person.api.repository.UsuarioRepository;
import com.bbm.person.api.service.ReportService;
import com.bbm.person.api.service.UsuarioService;

@CrossOrigin
@RestController
@RequestMapping("/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ReportService reportService;

	@PostMapping(value = "/", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Usuario> saveUser(@Valid @RequestBody Usuario usuario) {

		Usuario user = usuarioService.saveUser(usuario);

		usuarioService.insereUserPadrao(usuario.getId());

		return new ResponseEntity<Usuario>(user, HttpStatus.CREATED);
	}

	@GetMapping(value = "/", produces = "application/json")
	@CacheEvict(value = "cacheUsuario", allEntries = true)
	@CachePut("cacheUsuario")
	public ResponseEntity<Page<Usuario>> findAll() throws InterruptedException {

		PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("fullName"));

		Page<Usuario> usuarios = usuarioRepository.findAll(pageRequest);

		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);
	}

	@GetMapping(value = "/page/{page}", produces = "application/json")
	@CacheEvict(value = "cacheUsuario", allEntries = true)
	@CachePut("cacheUsuario")
	public ResponseEntity<Page<Usuario>> findAllPerPage(@PathVariable("page") int page) throws InterruptedException {

		PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("fullName"));

		Page<Usuario> usuarios = usuarioRepository.findAll(pageRequest);

		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);
	}

	@GetMapping(value = "/nome/{nome}", produces = "application/json")
	@CachePut("cacheUsuario")
	public ResponseEntity<Page<Usuario>> findByName(@PathVariable("nome") Optional<String> nome) throws InterruptedException {

		PageRequest pageRequest = null;
		Page<Usuario> usuarios = null;

		if (nome == null || (nome != null && nome.toString().trim().isEmpty()) 
				|| nome.toString().equalsIgnoreCase("undefined")) {

			pageRequest = PageRequest.of(0, 8, Sort.by("fullName"));
			usuarios = usuarioRepository.findAll(pageRequest);

		} else {
			pageRequest = PageRequest.of(0, 8, Sort.by("fullName"));
			usuarios = usuarioRepository.findByName(nome.orElse("").toString().trim().toUpperCase(), pageRequest);
		}

		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);
	}
	
	@GetMapping(value = "/nome/{nome}/page/{page}", produces = "application/json")
	@CachePut("cacheUsuario")
	public ResponseEntity<Page<Usuario>> findByNamePage(@PathVariable("nome") Optional<String> nome,
			@PathVariable("page") int page) throws InterruptedException {

		PageRequest pageRequest = null;
		Page<Usuario> usuarios = null;

		if (nome == null || (nome != null && nome.toString().trim().isEmpty()) 
				|| nome.toString().equalsIgnoreCase("undefined")) {

			pageRequest = PageRequest.of(page, 8, Sort.by("fullName"));
			usuarios = usuarioRepository.findAll(pageRequest);

		} else {
			pageRequest = PageRequest.of(page, 8, Sort.by("fullName"));
			usuarios = usuarioRepository.findByName(nome.orElse("").toString().trim().toUpperCase(), pageRequest);
		}

		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> findById(@PathVariable("id") Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}

	@PutMapping(value = "/", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> updateUser(@Valid @RequestBody Usuario usuario) {

		if (usuario.getId() == null) {

			return new ResponseEntity<String>("Usuário não encontrado para edicão", HttpStatus.OK);

		} else {

			Usuario user = usuarioService.updateUser(usuario);

			return new ResponseEntity<Usuario>(user, HttpStatus.OK);
		}

	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {

		usuarioRepository.deleteById(id);

		return new ResponseEntity<String>("Usuário Deletado com Sucesso", HttpStatus.OK);

	}

	@DeleteMapping(value = "/removeEndereco/{id}", produces = "application/text")
	public String deleteEndereco(@PathVariable("id") Long id) {

		enderecoRepository.deleteById(id);

		return "Deletado Com Sucesso";
	}
	
	@GetMapping(value = "/report", produces = "application/text")
	public ResponseEntity<String> downloadReport(HttpServletRequest request) throws Exception{
		
		byte[] pdf = reportService.gerarRelatorio("usuario", new HashedMap(), request.getServletContext());
		
		String base64Pdf = "data:application/pdf;base64,"  + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
	}
	
	@PostMapping(value = "/report/", produces = "application/text")
	public ResponseEntity<String> downloadReportParam(HttpServletRequest request, 
			@RequestBody UserReport userReport)throws Exception{
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
		String dataInicial = dateFormat.format(format.parse(userReport.getDataInicial()));
		String dataFinal = dateFormat.format(format.parse(userReport.getDataFinal()));
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("DATA_INICIAL", dataInicial);
		params.put("DATA_FINAL", dataFinal);
		
		byte[] pdf = reportService.gerarRelatorio("usuarioParam", params, request.getServletContext());
		
		String base64Pdf = "data:application/pdf;base64,"  + Base64.encodeBase64String(pdf);
		
		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
	}
}
