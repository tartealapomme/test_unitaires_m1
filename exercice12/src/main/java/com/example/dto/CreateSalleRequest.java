package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateSalleRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Min(value = 1, message = "La capacité doit être supérieure ou égale à 1")
    private int capacite;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
}
