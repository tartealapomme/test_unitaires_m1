package com.example.dto;

import com.example.model.Priorite;
import com.example.model.StatutTicket;
import com.example.model.Ticket;

public class TicketResponse {

    private final Long id;
    private final String titre;
    private final Priorite priorite;
    private final StatutTicket statut;

    public TicketResponse(Long id, String titre, Priorite priorite, StatutTicket statut) {
        this.id = id;
        this.titre = titre;
        this.priorite = priorite;
        this.statut = statut;
    }

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitre(),
                ticket.getPriorite(),
                ticket.getStatut()
        );
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
}
