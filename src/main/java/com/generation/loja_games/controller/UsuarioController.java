package com.generation.loja_games.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.loja_games.model.Usuario;
import com.generation.loja_games.model.UsuarioLogin;
import com.generation.loja_games.repository.UsuarioRepository;
import com.generation.loja_games.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    // Removida a injeção desnecessária de CategoriaController
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usuarioService;

    // Removido o construtor desnecessário
    
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(usuarios);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElse(ResponseEntity.notFound().build());
    }
	
	@PostMapping("/cadastrar")
		public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {
			return usuarioService.cadastrarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
				// Lança exceção do service se o cadastro falhar (e.g., usuário já existe ou menor de idade)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao cadastrar usuário!")); 
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {
		
		// Chama o serviço correto para ATUALIZAR o usuário
		return usuarioService.atualizarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				// Lança exceção do service se a atualização falhar (e.g., usuário não encontrado, e-mail de outro usuário, menor de idade)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado ou erro ao atualizar!"));  
	}
	
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> autenticar(@Valid @RequestBody Optional<UsuarioLogin> usuarioLogin) {
		return usuarioService.autenticarUsuario(usuarioLogin)
			.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos!")); // Lança exceção do service
	}
}

