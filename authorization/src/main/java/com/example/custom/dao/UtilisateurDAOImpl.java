package com.example.custom.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.example.custom.entities.Utilisateur;

@Repository
public class UtilisateurDAOImpl extends AbstractDAO implements UtilisateurDAO {

	@SuppressWarnings("unchecked")
	@Override
	public Utilisateur recupererPersonneParLogin(String login) {

		List<Utilisateur> utilisateurs = this.recupererSessionCourrante()
				.createCriteria(Utilisateur.class)
				.add(Restrictions.eq("login", login).ignoreCase()).list();
		if (utilisateurs.isEmpty()) {
			return null;
		} else {
			return utilisateurs.get(0);
		}

	}

}
