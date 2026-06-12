package com.example;

import java.util.Optional;

public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private String dernierMessageErreur;

    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public String getDernierMessageErreur() {
        return dernierMessageErreur;
    }

    public Optional<String> creerCompte(String email, String nomUtilisateur, String motDePasse) {
        throw new NotImplementedException();
    }

    public Optional<String> connecter(String nomUtilisateur, String motDePasse) {
        throw new NotImplementedException();
    }
}
