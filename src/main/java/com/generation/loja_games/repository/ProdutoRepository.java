package com.generation.loja_games.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.generation.loja_games.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	List<Produto> findAllByNomeContainingIgnoreCase(String nome);
	List<Produto> findByPrecoGreaterThan(BigDecimal preco);  
    List<Produto> findByPrecoLessThan(BigDecimal preco);
}
