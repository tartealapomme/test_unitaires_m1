package com.example.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateCompteRequest {

    @NotBlank(message = "Le numéro de compte est obligatoire")
    private String numero;

    @NotBlank(message = "Le titulaire est obligatoire")
    private String titulaire;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(String titulaire) {
        this.titulaire = titulaire;
    }
}
