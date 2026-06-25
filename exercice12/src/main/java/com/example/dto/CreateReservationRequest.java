package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateReservationRequest {

    @NotNull(message = "L'identifiant de la salle est obligatoire")
    private Long salleId;

    @NotBlank(message = "Le nom de la personne est obligatoire")
    private String nomPersonne;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime debut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime fin;

    public Long getSalleId() {
        return salleId;
    }

    public void setSalleId(Long salleId) {
        this.salleId = salleId;
    }

    public String getNomPersonne() {
        return nomPersonne;
    }

    public void setNomPersonne(String nomPersonne) {
        this.nomPersonne = nomPersonne;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }
}
