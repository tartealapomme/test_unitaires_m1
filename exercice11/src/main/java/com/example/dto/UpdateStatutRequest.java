package com.example.dto;

import com.example.model.StatutTicket;
import jakarta.validation.constraints.NotNull;

public class UpdateStatutRequest {

    @NotNull(message = "Le statut est obligatoire")
    private StatutTicket statut;

    public StatutTicket getStatut() {
        return statut;
    }

    public void setStatut(StatutTicket statut) {
        this.statut = statut;
    }
}
