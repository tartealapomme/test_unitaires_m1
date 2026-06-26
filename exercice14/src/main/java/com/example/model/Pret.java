package com.example.model;

import java.time.LocalDate;

public class Pret {

    private final String id;
    private final String adherentId;
    private final String ouvrageIsbn;
    private final LocalDate datePret;
    private final LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;

    public Pret(String id, String adherentId, String ouvrageIsbn,
                  LocalDate datePret, LocalDate dateRetourPrevue) {
        this.id = id;
        this.adherentId = adherentId;
        this.ouvrageIsbn = ouvrageIsbn;
        this.datePret = datePret;
        this.dateRetourPrevue = dateRetourPrevue;
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

    public LocalDate getDatePret() {
        return datePret;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public boolean estEnCours() {
        return dateRetourEffective == null;
    }
}
