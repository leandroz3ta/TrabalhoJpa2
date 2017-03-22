package com.z3tasistemas.modelo.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.AfterClass;
import org.junit.Test;

import com.z3tasistemas.base.test.Basetest;
import com.z3tasistemas.modelo.Materia;
import com.z3tasistemas.util.JPAUtil;

import static org.junit.Assert.*;

public class MateriaTest extends Basetest{

	@Test
	public void deveAlterarMateria(){
		
		deveSalvarMateria();
		TypedQuery<Materia>query = em.createQuery("SELECT m FROM Materia m", Materia.class).setMaxResults(1);
		
		Materia materia = query.getSingleResult();
		
		assertNotNull("Deve ter encontrado uma materia", materia);
		
		Integer versao = materia.getVersion();
		
		em.getTransaction().begin();
		
		materia.setNome("Português");
		
		materia = em.merge(materia);
		
		em.getTransaction().commit();
		
		assertNotEquals("Deve ter uma versão incrementada", versao.intValue(), materia.getVersion().intValue());
	}
	
	@Test
	public void devePesquisarMateria(){
		for(int i = 0; i < 10; i++){
			deveSalvarMateria();
		}
		
		TypedQuery<Materia>query = em.createQuery("SELECT m FROM Materia m", Materia.class);
		List<Materia>materias = query.getResultList();
		
		assertFalse("Deve ter encontrado uma matéria", materias.isEmpty());
		assertTrue("Deve ter encontrado várias matérias", materias.size()>=10);
	}
	
	@Test
	public void deveExcluirMateria(){
		deveSalvarMateria();
		
		TypedQuery<Long>query = em.createQuery("SELECT MAX(m.id) FROM Materia m", Long.class);
		
		Long id = query.getSingleResult();
		
		em.getTransaction().begin();
		
		Materia materia = em.find(Materia.class, id);
		em.remove(materia);
		
		em.getTransaction().commit();
		
		Materia materiaExcluida = em.find(Materia.class, id);
		assertNull("Não deve ter encontrado a matéria", materiaExcluida);
	}
	
	@Test
	public void deveSalvarMateria(){
		Materia materia = new Materia().setNome("Matemática");
		assertTrue("Não deve ter id definido", materia.isTransient());
		
		em.getTransaction().begin();
		
		em.persist(materia);
		
		em.getTransaction().commit();
		
		assertNotNull("Deve ter id definido", materia.getId());
	}
	
	@AfterClass
	public static void deveLimparBaseTeste(){
		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();
		
		entityManager.getTransaction().begin();
		
		Query query = entityManager.createQuery("DELETE FROM Materia m");
		
		int  qtdRegistrosExcluidos = query.executeUpdate();
		
		entityManager.getTransaction().commit();

		assertTrue("certifica que a base foi limpada", qtdRegistrosExcluidos > 0);
	}
}
