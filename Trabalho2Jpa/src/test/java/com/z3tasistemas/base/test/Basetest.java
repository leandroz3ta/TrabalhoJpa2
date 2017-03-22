package com.z3tasistemas.base.test;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;

import com.z3tasistemas.util.JPAUtil;

public abstract class Basetest {

	protected EntityManager em;
	
	public Session getSession() {
		return (Session) em.getDelegate();
	}
	
	public Criteria createCriteria(Class<?> clazz) {
		return getSession().createCriteria(clazz);
	}
	
	public Criteria createCriteria(Class<?> clazz, String alias) {
		return getSession().createCriteria(clazz, alias);
	}

	@Before
	public void instanciarEntityManager() {
		em = JPAUtil.INSTANCE.getEntityManager();
	}
	
	@After
	public void fecharEntityManager() {
		if (em.isOpen()) {
			em.close();
		}
	}
}
