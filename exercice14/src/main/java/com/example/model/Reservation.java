package com.example.model;

import java.time.LocalDateTime;

public class Reservation {

    private final String id;
    private final String adherentId;
    private final String ouvrageIsbn;
    private final LocalDateTime dateReservation;

    public Reservation(String id, String adherentId, String ouvrageIsbn, LocalDateTime dateReservation) {
        this.id = id;
        this.adherentId = adherentId;
        this.ouvrageIsbn = ouvrageIsbn;
        this.dateReservation = dateReservation;
    }

    public String getId() {
        return id;
    }

    public String getAdherentId() {
        return adherentId;
    }

    public String getOuvrageIsbn() {
        return ouvrageIsbn;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }
}
