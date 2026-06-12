package com.example.model;

public class Ticket {

    private final Long id;
    private final String titre;
    private final Priorite priorite;
    private StatutTicket statut;

    public Ticket(Long id, String titre, Priorite priorite, StatutTicket statut) {
        this.id = id;
        this.titre = titre;
        this.priorite = priorite;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public StatutTicket getStatut() {
        return statut;
    }

    public void setStatut(StatutTicket statut) {
        this.statut = statut;
    }
}
