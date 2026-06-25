package com.example.model;

import java.time.LocalDateTime;

public class Reservation {

    private final Long id;
    private final Long salleId;
    private final String nomPersonne;
    private final LocalDateTime debut;
    private final LocalDateTime fin;
    private StatutReservation statut;

    public Reservation(Long id, Long salleId, String nomPersonne,
                       LocalDateTime debut, LocalDateTime fin, StatutReservation statut) {
        this.id = id;
        this.salleId = salleId;
        this.nomPersonne = nomPersonne;
        this.debut = debut;
        this.fin = fin;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public Long getSalleId() {
        return salleId;
    }

    public String getNomPersonne() {
        return nomPersonne;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }
}
