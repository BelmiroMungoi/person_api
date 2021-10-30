package com.bbm.person.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bbm.person.api.model.Usuario;
import com.bbm.person.api.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Usuario saveUser(Usuario usuario) {
		for (int i = 0; i < usuario.getEnderecos().size(); i++) {
			usuario.getEnderecos().get(i).setUsuario(usuario);
		}

		String senhaCript = new BCryptPasswordEncoder().encode(usuario.getPassWord());
		usuario.setPassWord(senhaCript);

		return usuarioRepository.save(usuario);

	}

	public Usuario updateUser(Usuario usuario) {

		for (int i = 0; i < usuario.getEnderecos().size(); i++) {
			usuario.getEnderecos().get(i).setUsuario(usuario);
		}

		Usuario userTemp = usuarioRepository.findById(usuario.getId()).get();

		// Caso a senha inserida senha nova ira criptografar para atualizar
		if (!userTemp.getPassWord().equals(usuario.getPassWord())) {
			String senhaCript = new BCryptPasswordEncoder().encode(usuario.getPassWord());
			usuario.setPassWord(senhaCript);
		}
		return usuarioRepository.saveAndFlush(usuario);
	}

	public void insereUserPadrao(Long id) {
		// Descobre a constraint
		String constraint = usuarioRepository.consultaConstraint();

		if (constraint != null) {
			// Remove a constraint
			jdbcTemplate.execute(" alter table usuario_roles drop constraint " + constraint);
		}
		
		// Insere Usuario padrao
		usuarioRepository.insereAcessoPadrao(id);
	}

}
