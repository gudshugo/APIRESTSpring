package com.aulas.cursoangular.dto;

import java.io.Serializable;

import com.aulas.cursoangular.domain.Produto;

public class ProdutoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String nome;
	
	private Double preço;
	
	public ProdutoDTO() {
		
	}
	
	public ProdutoDTO(Produto obj) {
		id = obj.getId();
		nome = obj.getNome();
		preço = obj.getPreço();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPreço() {
		return preço;
	}

	public void setPreço(Double preço) {
		this.preço = preço;
	}
	
	
}
