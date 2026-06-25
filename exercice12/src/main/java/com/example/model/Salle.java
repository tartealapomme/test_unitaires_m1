package com.example.model;

public class Salle {

    private final Long id;
    private final String nom;
    private final int capacite;

    public Salle(Long id, String nom, int capacite) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
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
