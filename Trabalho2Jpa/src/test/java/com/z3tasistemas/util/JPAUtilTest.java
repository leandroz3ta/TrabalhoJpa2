package com.z3tasistemas.util;

import org.junit.Test;

import com.z3tasistemas.base.test.Basetest;

import static org.junit.Assert.*;

public class JPAUtilTest extends Basetest{

	@Test
	public void deveTerInstanciaDoEntityManagerDefinida() {
		assertNotNull("instância do EntityManager não deve estar nula", em);
	}
	
	@Test
	public void deveFecharEntityManager() {
		em.close();
		
		assertFalse("instância do EntityManager deve estar fechada", em.isOpen());
	}
	
	@Test
	public void deveAbrirUmaTransacao() {
		assertFalse("transação deve estar fechada", em.getTransaction().isActive());
		
		em.getTransaction().begin();
		
		assertTrue("transação deve estar aberta", em.getTransaction().isActive());
	}
}
