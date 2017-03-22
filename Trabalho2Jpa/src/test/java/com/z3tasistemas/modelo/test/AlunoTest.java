package com.z3tasistemas.modelo.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.junit.AfterClass;
import org.junit.Test;

import com.z3tasistemas.base.test.Basetest;
import com.z3tasistemas.modelo.Aluno;
import com.z3tasistemas.util.JPAUtil;

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
		query.setParameter("rg", RG_PADRAO);
		
		List<Aluno> aluno = query.getResultList();
		
		assertFalse("verifica se h� registros na lista", aluno.isEmpty());
		
		aluno.forEach(alunos -> {
			assertNull("verifica que o cpf deve estar null", alunos.getRg());
			
			alunos.setRg(RG_PADRAO);
		});
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarIdNome() {
		salvarAluno();
		
		Query query = em.createQuery("SELECT a.id, a.nome FROM Aluno a WHERE a.rg = :rg");
		query.setParameter("rg", RG_PADRAO);
		
		List<Object[]> resultado = query.getResultList();
		
		assertFalse("verifica se há registros na lista", resultado.isEmpty());
		
		resultado.forEach(linha -> {
			assertTrue("verifica que o primeiro item é o ID", 	linha[0] instanceof Long);
			assertTrue("verifica que o segundo item é o nome", 	linha[1] instanceof String);
			
			Aluno aluno = new Aluno((Long) linha[0], (String) linha[1]);
			assertNotNull(aluno);
		});
	}
	
	@Test
	public void deveVerificarExistenciaCliente() {
		salvarAluno();
		
		Query query = em.createQuery("SELECT COUNT(a.id) FROM Aluno a WHERE a.rg = :rg");
		query.setParameter("rg", RG_PADRAO);
		
		Long qtdResultados = (Long) query.getSingleResult();

		assertTrue("verifica se há registros na lista", qtdResultados > 0L);
	}
	
	@Test(expected = NonUniqueResultException.class)
	public void naoDeveFuncionarSingleResultComMuitosRegistros() {
		salvarAluno();
		salvarAluno();
		
		Query query = em.createQuery("SELECT a.id FROM Aluno a WHERE a.rg = :rg");
		query.setParameter("rg", RG_PADRAO);
		
		query.getSingleResult();
		
		fail("método getSingleResult deve disparar exception NonUniqueResultException");
	}
	
	@Test(expected = NoResultException.class)
	public void naoDeveFuncionarSingleResultComNenhumRegistro() {
		salvarAluno();
		salvarAluno();
		
		Query query = em.createQuery("SELECT a.id FROM Aluno a WHERE a.rg = :rg");
		query.setParameter("rg", "000000-00");
		
		query.getSingleResult();
		
		fail("método getSingleResult deve disparar exception NoResultException");
	}
	
	@Test
	public void deveAcessarAtributoLazy() {
		salvarAluno();
		
		Aluno aluno = em.find(Aluno.class, 1L);
		
		assertNotNull("verifica se encontrou um registro", aluno);
		
		assertNotNull("lista lazy não deve ser null", aluno.getNotas());
	}
	
	@Test(expected = LazyInitializationException.class)
	public void naoDeveAcessarAtributoLazyForaEscopoEntityManager() {
		salvarAluno();
		
		Aluno aluno = em.find(Aluno.class, 1L);
		
		assertNotNull("verifica se encontrou um registro", aluno);

		em.detach(aluno);
		
		aluno.getNotas().size();
		
		fail("deve disparar LazyInitializationException ao acessar atributo lazy de um objeto fora de escopo do EntityManager");
	}
	
	@AfterClass
	public static void deveLimparBaseTeste() {
		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();
		
		entityManager.getTransaction().begin();
		
		Query query = entityManager.createQuery("DELETE FROM Aluno a");
		int qtdRegistrosExcluidos = query.executeUpdate();
		
		entityManager.getTransaction().commit();

		assertTrue("certifica que a base foi limpada", qtdRegistrosExcluidos > 0);
		
		LOGGER.info("============ Base de testes limpada (Tabela Aluno) ============");
	}
}
