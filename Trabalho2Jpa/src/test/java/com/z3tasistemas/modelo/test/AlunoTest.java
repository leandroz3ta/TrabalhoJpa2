package com.z3tasistemas.modelo.test;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.z3tasistemas.base.test.Basetest;
import com.z3tasistemas.modelo.Aluno;

import static org.junit.Assert.*;

public class AlunoTest extends Basetest{

	private static final String RG_PADRAO = "01234567-9";
	
	private static final Logger LOGGER = Logger.getLogger(AlunoTest.class);
	
	@Test
	public void salvarAluno(){
		Aluno aluno = new Aluno()
		.setNome("Raimundo Nonato")
		.setRg(RG_PADRAO);
		
		assertTrue("Deve ter ID definido", aluno.isTransient());
		
		em.getTransaction().begin();
		em.persist(aluno);
		em.getTransaction().commit();
		
		assertFalse("Deve ter ID definido", aluno.isTransient());
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarRg(){
		salvarAluno();
		
		String filtro  = "Nonato";
		
		Query query = em.createQuery("SELECT a.rg FROM Aluno a WHERE a.nome LIKE :nome");
		query.setParameter("nome", "%".concat(filtro).concat("%"));
		
		List<String>listaRG = query.getResultList();
		
		assertFalse("Verifica se h� registros na lista", listaRG.isEmpty());
		
		listaRG.forEach(rg -> LOGGER.info("\n\n============= RG: "+rg+"\n\n"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarAlunoComIdNome() {
		salvarAluno();
		
		Query query = em.createQuery("SELECT new Aluno(a.id, a.nome) FROM Aluno a WHERE a.rg = :rg");
		query.setParameter("RG", RG_PADRAO);
		
		List<Aluno> aluno = query.getResultList();
		
		assertFalse("verifica se h� registros na lista", aluno.isEmpty());
		
		aluno.forEach(alunos -> {
			assertNull("verifica que o cpf deve estar null", alunos.getRg());
			
			alunos.setRg(RG_PADRAO);
		});
	}
	

}
