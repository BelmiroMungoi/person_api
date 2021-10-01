package com.bbm.person.api.model.dto;

import java.io.Serializable;
import java.util.List;

import com.bbm.person.api.model.Usuario;

public class UsuarioDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nomeCompleto;
	private String nomeUsuario;
	private List<?> endereco;

	public UsuarioDto(Usuario usuario) {
		this.nomeCompleto = usuario.getFullName();
		this.nomeUsuario = usuario.getUserName();
		this.endereco = usuario.getEnderecos();
	}
	
	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public List<?> getEndereco() {
		return endereco;
	}

	public void setEndereco(List<?> endereco) {
		this.endereco = endereco;
	}

}
