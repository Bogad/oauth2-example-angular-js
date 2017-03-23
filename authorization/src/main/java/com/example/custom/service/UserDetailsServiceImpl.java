package com.example.custom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.custom.dao.UtilisateurDAO;
import com.example.custom.entities.Utilisateur;

@Service(value = "userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UtilisateurDAO utilisateurDAO;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		Utilisateur utilisateur = null;
		try {
			utilisateur = utilisateurDAO.recupererPersonneParLogin(username);
		} catch (Exception e) {
			throw new UsernameNotFoundException("", e);
		}

		if (utilisateur == null) {
			throw new UsernameNotFoundException(username);
		}

		return utilisateur;
	}

}
