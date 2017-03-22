package com.z3tasistemas.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_materia")
@Entity
public class Materia extends BaseEntity<Long>{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1620251909914327813L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_materia", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	public Materia(){}

	public Materia(Long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public Materia setNome(String nome) {
		this.nome = nome;
		return this;
	}

	
	
}
