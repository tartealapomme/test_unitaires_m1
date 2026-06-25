package com.example.dto;

import com.example.model.Reservation;
import com.example.model.StatutReservation;

import java.time.LocalDateTime;

public class ReservationResponse {

    private final Long id;
    private final Long salleId;
    private final String nomPersonne;
    private final LocalDateTime debut;
    private final LocalDateTime fin;
    private final StatutReservation statut;

    public ReservationResponse(Long id, Long salleId, String nomPersonne,
                               LocalDateTime debut, LocalDateTime fin, StatutReservation statut) {
        this.id = id;
        this.salleId = salleId;
        this.nomPersonne = nomPersonne;
        this.debut = debut;
        this.fin = fin;
        this.statut = statut;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSalleId(),
                reservation.getNomPersonne(),
                reservation.getDebut(),
                reservation.getFin(),
                reservation.getStatut()
        );
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
}
