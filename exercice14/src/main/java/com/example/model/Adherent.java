package com.example.model;

public class Adherent {

    private final String id;
    private final String nom;
    private boolean suspendu;

    public Adherent(String id, String nom) {
        this.id = id;
        this.nom = nom;
        this.suspendu = false;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public boolean isSuspendu() {
        return suspendu;
    }

    public void setSuspendu(boolean suspendu) {
        this.suspendu = suspendu;
    }
}
