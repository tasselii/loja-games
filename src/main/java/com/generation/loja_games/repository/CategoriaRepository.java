package com.generation.loja_games.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.generation.loja_games.model.Categoria;
import com.generation.loja_games.model.Usuario;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	List<Categoria> findAllByTituloContainingIgnoreCase(String titulo);
}