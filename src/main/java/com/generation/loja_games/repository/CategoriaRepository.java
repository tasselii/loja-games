package com.generation.loja_games.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.generation.loja_games.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	/* SELECT * FROM tb_pontagens WHERE titulo LIKE "%?%"; */
	List<Categoria> findAllByTituloContainingIgnoreCase(String titulo);
}