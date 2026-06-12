package com.example.controller;

import com.example.dto.CreateTicketRequest;
import com.example.dto.TicketResponse;
import com.example.dto.UpdateStatutRequest;
import com.example.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> creer(@Valid @RequestBody CreateTicketRequest request) {
        var ticket = ticketService.creer(request.getTitre(), request.getPriorite());
        var response = TicketResponse.from(ticket);
        return ResponseEntity.created(URI.create("/api/tickets/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(TicketResponse.from(ticketService.trouverParId(id)));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> listerTous() {
        var tickets = ticketService.listerTous().stream()
                .map(TicketResponse::from)
                .toList();
        return ResponseEntity.ok(tickets);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> modifierStatut(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatutRequest request) {
        var ticket = ticketService.modifierStatut(id, request.getStatut());
        return ResponseEntity.ok(TicketResponse.from(ticket));
    }
}
