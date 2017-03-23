package com.example.custom.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractDAO {

	@Autowired
	private SessionFactory sessionFactory;

	protected void enregisterEntite(Object entite) {
		recupererSessionCourrante().save(entite);
	}

	protected Session recupererSessionCourrante() {
		return this.sessionFactory.getCurrentSession();
	}

}
