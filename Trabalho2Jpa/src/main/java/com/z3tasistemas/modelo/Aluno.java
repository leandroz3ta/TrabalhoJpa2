package com.z3tasistemas.modelo;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Aluno extends BaseEntity<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1922594820693444973L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aluno", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "nome_aluno", nullable = false, length = 100)
	private String nome;
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "rg_aluno", length = 10)
	private String rg;
	
	
	@OneToMany(mappedBy = "aluno", fetch = FetchType.LAZY)
	private List<Historico>notas;

	public List<Historico>getHistorico(){
		return notas;
	}
	
	public Aluno() {}

	public Aluno(String nome, String rg) {
		super();
		this.id = id;
		this.nome = nome;
		this.rg = rg;
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

	public Aluno setNome(String nome) {
		this.nome = nome;
				return this;
	}

	public String getRg() {
		return rg;
	}

	public Aluno setRg(String rg) {
		this.rg = rg;
		return this;
	}
	
	
}
