package com.example.service;

import com.example.exception.InvalidStatusTransitionException;
import com.example.exception.TicketNotFoundException;
import com.example.model.Priorite;
import com.example.model.StatutTicket;
import com.example.model.Ticket;
import com.example.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket creer(String titre, Priorite priorite) {
        if (titre == null || titre.trim().length() < 3) {
            throw new IllegalArgumentException("Le titre doit contenir au moins 3 caractères");
        }
        if (priorite == null) {
            throw new IllegalArgumentException("La priorité est obligatoire");
        }
        return ticketRepository.save(titre.trim(), priorite, StatutTicket.OPEN);
    }

    public Ticket trouverParId(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<Ticket> listerTous() {
        return ticketRepository.findAll();
    }

    public Ticket modifierStatut(Long id, StatutTicket nouveauStatut) {
        Ticket ticket = trouverParId(id);
        if (ticket.getStatut() == StatutTicket.RESOLVED) {
            throw new InvalidStatusTransitionException("Un ticket résolu ne peut plus changer de statut");
        }
        if (!transitionAutorisee(ticket.getStatut(), nouveauStatut)) {
            throw new InvalidStatusTransitionException("Transition de statut non autorisée");
        }
        ticket.setStatut(nouveauStatut);
        return ticket;
    }

    public void supprimerTous() {
        ticketRepository.deleteAll();
    }

    private boolean transitionAutorisee(StatutTicket actuel, StatutTicket nouveau) {
        return switch (actuel) {
            case OPEN -> nouveau == StatutTicket.IN_PROGRESS || nouveau == StatutTicket.RESOLVED;
            case IN_PROGRESS -> nouveau == StatutTicket.RESOLVED;
            case RESOLVED -> false;
        };
    }
}
