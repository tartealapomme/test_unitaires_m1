package com.example;

import java.util.Optional;

public interface UtilisateurRepository {
    Optional<Utilisateur> findByNomUtilisateur(String nomUtilisateur);

    boolean existsByNomUtilisateur(String nomUtilisateur);

    void save(Utilisateur utilisateur);
}
