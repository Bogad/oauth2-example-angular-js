package com.example.custom.dao;

import com.example.custom.entities.Utilisateur;

public interface UtilisateurDAO {

	Utilisateur recupererPersonneParLogin(String login);

}
