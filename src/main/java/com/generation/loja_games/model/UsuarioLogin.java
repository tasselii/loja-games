package com.generation.loja_games.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UsuarioLogin {
	
	private Long id;
	private String nome;
	private String usuario;
	private String senha;
	private String token;

	// Adiciona o campo dataNascimento
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate dataNascimento;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	// Getter e Setter para dataNascimento
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

}
