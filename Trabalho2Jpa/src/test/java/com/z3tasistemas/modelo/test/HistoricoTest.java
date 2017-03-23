package com.z3tasistemas.modelo.test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

import com.z3tasistemas.base.test.Basetest;
import com.z3tasistemas.modelo.Aluno;
import com.z3tasistemas.modelo.Historico;
import com.z3tasistemas.modelo.Materia;
import com.z3tasistemas.util.JPAUtil;

public class HistoricoTest extends Basetest{

	private static final String RG_PADRAO = "010188-10";
	private static final Logger LOGGER = Logger.getLogger(AlunoTest.class);
	
	
	@Test
	public void deveSalvarHistoricoComRelacionamentosEmCascadta() {
		Historico historico = criarHistorico();
		
		historico.getMaterias().add(criarMateria("Português"));
		historico.getMaterias().add(criarMateria("Matemàtica"));
		
		assertTrue("não deve ter ID definido", historico.isTransient());
		
		em.getTransaction().begin();
		em.persist(historico);
		em.getTransaction().commit();

		assertFalse("deve ter ID definido", historico.isTransient());
		assertFalse("deve ter ID definido", historico.getAluno().isTransient());
		
		historico.getMaterias().forEach(historicos -> assertFalse("deve ter ID definido", historico.isTransient()));
	}
	
	@Test(expected = IllegalStateException.class)
	public void naoDeveFazerMergeEmObjetosTransient() {
		Historico historico = criarHistorico();
		
		historico.getMaterias().add(criarMateria("Biologia"));
		historico.getMaterias().add(criarMateria("Ingles"));
		
		assertTrue("não deve ter ID definido", historico.isTransient());
		
		em.getTransaction().begin();
		historico = em.merge(historico);
		em.getTransaction().commit();
		
		fail("não deveria ter salvo (merge) uma matéria nova com relacionamentos transient");
	}
	
	@Test
	public void deveConsultarQuantidadeMaterias() {
		Historico historico = criarHistorico("001001001-01");
		
		for (int i = 0; i < 10; i++) {
			historico.getMaterias().add(criarMateria("Ingles"+i));
		}
		
		em.getTransaction().begin();
		em.persist(historico);
		em.getTransaction().commit();
		
		assertFalse("deve ter persistido a matéria", historico.isTransient());
		
		int qtdMateriasAdicionadas = historico.getMaterias().size();
		
		assertTrue("lista de materias deve ter itens", qtdMateriasAdicionadas > 0);
		
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT COUNT(m.id) ");
		jpql.append("   FROM Historico h ");
		jpql.append("  INNER JOIN h.materias m ");
		jpql.append("  INNER JOIN h.aluno a ");
		jpql.append("  WHERE a.cpf = :cpf ");
		
		Query query = em.createQuery(jpql.toString());
		query.setParameter("rg", "00100-01");

		Long qtdMateriasNoHistorico = (Long) query.getSingleResult();
		
		assertEquals("quantidade de materias deve ser igual a quantidade da lista de meterias", 
				qtdMateriasNoHistorico.intValue(), qtdMateriasAdicionadas);
	}
	

	@AfterClass
	public static void deveLimparBaseTeste() {
		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();
		
		entityManager.getTransaction().begin();
		
		Query query = entityManager.createQuery("DELETE FROM Historico h");
		int qtdRegistrosExcluidos = query.executeUpdate();
		
		entityManager.getTransaction().commit();

		assertTrue("certifica que a base foi limpada", qtdRegistrosExcluidos > 0);
		
		LOGGER.info("============ Base de testes limpada (Tabela Historico) ============");
	}
	
	private Materia criarMateria(String nome) {
		return new Materia()
				.setNome(nome);
	}
	
	private Historico criarHistorico() {
		return criarHistorico(null);
	}
	
	private Historico criarHistorico(String rg) {
		Aluno aluno = new Aluno()
				.setNome("Raimundo Barros")
				.setRg(rg == null ? RG_PADRAO : rg);

		assertTrue("não deve ter ID definido", aluno.isTransient());
		
		return new Historico()
				.setAluno(aluno);
	}
}
