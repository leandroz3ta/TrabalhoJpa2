package com.z3tasistemas.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ManyToAny;

@Entity
public class Historico extends BaseEntity<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8864502939848627557L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_historico", unique = true, nullable = false)
	private Long id;
	
	@ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_aluno", referencedColumnName = "id_aluno", insertable = true, updatable = false, nullable = false)
	private Aluno aluno;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
	@JoinTable(name = "nota", joinColumns = @JoinColumn(name = "id_historico"), inverseJoinColumns = @JoinColumn(name = "id_materia"))
	private List<Materia>materias;

	public Historico() {}

	public Historico(Long id, Aluno aluno, List<Materia> materias) {
		super();
		this.id = id;
		this.aluno = aluno;
		this.materias = materias;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public Historico setAluno(Aluno aluno) {
		this.aluno = aluno;
				return this;
	}

	public List<Materia> getMaterias() {
		if(materias == null){
			materias = new ArrayList<>();
		}
		return materias;
	}

	public void setMaterias(List<Materia> materias) {
		this.materias = materias;
	}
	
	
}
