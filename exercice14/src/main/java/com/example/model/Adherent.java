package com.example.model;

public class Adherent {

    private final String id;
    private final String nom;
    private boolean suspendu;
    private int anneeRetardsEnCours;
    private int retardsImportants;

    public Adherent(String id, String nom) {
        this.id = id;
        this.nom = nom;
        this.suspendu = false;
        this.anneeRetardsEnCours = 0;
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

    public int getAnneeRetardsEnCours() {
        return anneeRetardsEnCours;
    }

    public int getRetardsImportants() {
        return retardsImportants;
    }

    public void actualiserPourAnnee(int annee) {
        if (anneeRetardsEnCours == annee) {
            return;
        }
        boolean changementAnnee = anneeRetardsEnCours != 0;
        anneeRetardsEnCours = annee;
        if (changementAnnee) {
            retardsImportants = 0;
            suspendu = false;
        }
    }

    public void incrementerRetardImportant(int annee) {
        actualiserPourAnnee(annee);
        retardsImportants++;
    }
}
