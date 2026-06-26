package com.example.model;

public class Adherent {

    private final String id;
    private final String nom;
    private boolean suspendu;
    private int retardsImportants;

    public Adherent(String id, String nom) {
        this.id = id;
        this.nom = nom;
        this.suspendu = false;
        this.retardsImportants = 0;
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

    public int getRetardsImportants() {
        return retardsImportants;
    }

    public void incrementerRetardImportant() {
        retardsImportants++;
    }
}
