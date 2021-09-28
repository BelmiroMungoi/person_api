package com.bbm.person.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bbm.person.api.model.Usuario;
import com.bbm.person.api.repository.UsuarioRepository;

@Service
public class ImplementationUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioRepository.findByUserName(username);

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario não encontrado");
		}

		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getAuthorities());
	}

}
