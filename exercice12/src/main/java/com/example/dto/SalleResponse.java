package com.example.dto;

import com.example.model.Salle;

public class SalleResponse {

    private final Long id;
    private final String nom;
    private final int capacite;

    public SalleResponse(Long id, String nom, int capacite) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
    }

    public static SalleResponse from(Salle salle) {
        return new SalleResponse(salle.getId(), salle.getNom(), salle.getCapacite());
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getCapacite() {
        return capacite;
    }
}
