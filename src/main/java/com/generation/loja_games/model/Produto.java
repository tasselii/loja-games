package com.generation.loja_games.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produtos") // CREAT TABLE tb_produtos();
public class Produto {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    @NotBlank(message = "O atributo nome é obrigatório")
    @Pattern(regexp = "^[^0-9].*", message = "O nome não pode começar com número")
    @Size(min = 5, max = 100, message = "O atributo nome deve ter no mínimo 5 e no máximo 100 caracteres")
    private String nome;
    

    @Column(length = 1000)
    @NotBlank(message = "O atributo descrição é obrigatório")
    @Size(min = 10, max = 1000, message = "A descrição deve ter no mínimo 10 e no máximo 1000 caracteres")
    private String descricao;
    

    @Column(precision = 10, scale = 2)
    @NotNull(message = "O atributo preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
    private BigDecimal preco;
    

    @Column(length = 1000)
    @NotBlank(message = "O atributo imagem é obrigatório")
    @Size(min = 10, max = 1000, message = "O link da imagem deve ter no mínimo 10 e no máximo 1000 caracteres")
    @URL(message = "O atributo 'imagem' deve conter uma URL válida")
    private String imagem;
	
	@UpdateTimestamp
	private LocalDateTime data;
	
	@ManyToOne
	@JsonIgnoreProperties("produto") 
	private Categoria categoria;

	@ManyToOne
    @JoinColumn(name = "usuario_id") 
	@JsonIgnoreProperties("categoria") 
    private Usuario usuario; 

	// Getters e Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	// Getter e Setter para Categoria
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	// Getter e Setter para Usuario
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}

