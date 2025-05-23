package com.generation.loja_games.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.loja_games.model.Usuario;
import com.generation.loja_games.model.UsuarioLogin;
import com.generation.loja_games.repository.UsuarioRepository;
import com.generation.loja_games.security.JwtService;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário (e-mail) já existe!", null);
		}

		// Verifica se o usuário é maior de 18 anos
		if (!isMaiorDeIdade(usuario.getDataNascimento())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário deve ser maior de 18 anos!", null);
		}
		
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		return Optional.ofNullable(usuarioRepository.save(usuario));
	}
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
		
		// Verifica se o ID do usuário foi fornecido e se o usuário existe
		if(usuario.getId() == null || !usuarioRepository.existsById(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!", null);
        }

		// Busca o usuário existente no banco pelo ID
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());

		// Verifica se o usuário é maior de 18 anos ao atualizar
		if (!isMaiorDeIdade(usuario.getDataNascimento())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário deve ser maior de 18 anos!", null);
		}

		// Verifica se o email (usuário) já está sendo usado por OUTRO usuário
		Optional<Usuario> buscaUsuarioPorEmail = usuarioRepository.findByUsuario(usuario.getUsuario());

		// Se o email já existe E pertence a um usuário diferente do que está sendo atualizado
		if (buscaUsuarioPorEmail.isPresent() && !buscaUsuarioPorEmail.get().getId().equals(usuario.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário (e-mail) já está em uso por outra conta!", null);
        }

		// Criptografa a senha antes de salvar
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		// Salva as alterações
		return Optional.ofNullable(usuarioRepository.save(usuario));
		
	}
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {
		
		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha());
		
		Authentication authentication = authenticationManager.authenticate(credenciais);
		
		if(authentication.isAuthenticated()) {
			Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
			
			if(usuario.isPresent()) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setDataNascimento(usuario.get().getDataNascimento()); // Adiciona a data de nascimento ao login
				usuarioLogin.get().setSenha("");
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));
				
				return usuarioLogin;
			}
		}
		// Lança exceção se a autenticação falhar
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos!", null);
	} 
	
	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario);
	}
	
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}

	// Método privado para verificar se a data de nascimento corresponde a maioridade (18 anos)
	// Alterado para usar a lógica com minusYears, conforme solicitado
	private boolean isMaiorDeIdade(LocalDate dataNascimento) {
		if (dataNascimento == null) {
			return false; // Data de nascimento é obrigatória
		}
		// Verifica se a data de nascimento não é posterior à data exata de 18 anos atrás
		// Equivalente a dataNascimento <= hoje - 18 anos
		return !dataNascimento.isAfter(LocalDate.now().minusYears(18));
	}
}

