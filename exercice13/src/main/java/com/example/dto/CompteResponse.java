package com.example.dto;

import com.example.model.Compte;

import java.math.BigDecimal;

public class CompteResponse {

    private final String numero;
    private final String titulaire;
    private final BigDecimal solde;

    public CompteResponse(String numero, String titulaire, BigDecimal solde) {
        this.numero = numero;
        this.titulaire = titulaire;
        this.solde = solde;
    }

    public static CompteResponse from(Compte compte) {
        return new CompteResponse(compte.getNumero(), compte.getTitulaire(), compte.getSolde());
    }

    public String getNumero() {
        return numero;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public BigDecimal getSolde() {
        return solde;
    }
}
