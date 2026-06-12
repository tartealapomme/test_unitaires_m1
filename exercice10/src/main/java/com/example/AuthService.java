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
        if (utilisateurRepository.existsByNomUtilisateur(nomUtilisateur)) {
            return Optional.empty();
        }
        utilisateurRepository.save(new Utilisateur(email, nomUtilisateur, motDePasse));
        return Optional.of("Compte créé avec succès");
    }

    public Optional<String> connecter(String nomUtilisateur, String motDePasse) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByNomUtilisateur(nomUtilisateur);
        if (utilisateur.isEmpty() || !utilisateur.get().getMotDePasse().equals(motDePasse)) {
            dernierMessageErreur = "Identifiants invalides";
            return Optional.empty();
        }
        return Optional.of("Page d'accueil");
    }
}
