package com.example.controller;

import com.example.exception.InvalidStatusTransitionException;
import com.example.exception.TicketNotFoundException;
import com.example.model.Priorite;
import com.example.model.StatutTicket;
import com.example.model.Ticket;
import com.example.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TicketController.class, TicketExceptionHandler.class})
class TicketControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Test
    void shouldReturnCreated_whenPostBodyIsValid() throws Exception {
        when(ticketService.creer("Bug critique", Priorite.HIGH))
                .thenReturn(new Ticket(1L, "Bug critique", Priorite.HIGH, StatutTicket.OPEN));

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titre\":\"Bug critique\",\"priorite\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tickets/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titre").value("Bug critique"))
                .andExpect(jsonPath("$.priorite").value("HIGH"))
                .andExpect(jsonPath("$.statut").value("OPEN"));

        verify(ticketService).creer("Bug critique", Priorite.HIGH);
    }

    @Test
    void shouldReturnBadRequest_whenPostBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titre\":\"ab\",\"priorite\":\"HIGH\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Le titre doit contenir au moins 3 caractères"));

        verify(ticketService, never()).creer(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldReturnOk_whenTicketExists() throws Exception {
        when(ticketService.trouverParId(1L))
                .thenReturn(new Ticket(1L, "Bug critique", Priorite.HIGH, StatutTicket.OPEN));

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titre").value("Bug critique"));

        verify(ticketService).trouverParId(1L);
    }

    @Test
    void shouldReturnNotFound_whenTicketDoesNotExist() throws Exception {
        when(ticketService.trouverParId(99L)).thenThrow(new TicketNotFoundException(99L));

        mockMvc.perform(get("/api/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Aucun ticket trouvé avec l'identifiant 99"));

        verify(ticketService).trouverParId(99L);
    }

    @Test
    void shouldReturnOk_whenStatusUpdateIsValid() throws Exception {
        when(ticketService.modifierStatut(1L, StatutTicket.RESOLVED))
                .thenReturn(new Ticket(1L, "Bug critique", Priorite.HIGH, StatutTicket.RESOLVED));

        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statut\":\"RESOLVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("RESOLVED"));

        verify(ticketService).modifierStatut(1L, StatutTicket.RESOLVED);
    }

    @Test
    void shouldReturnConflict_whenStatusTransitionIsForbidden() throws Exception {
        when(ticketService.modifierStatut(1L, StatutTicket.IN_PROGRESS))
                .thenThrow(new InvalidStatusTransitionException("Un ticket résolu ne peut plus changer de statut"));

        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statut\":\"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Un ticket résolu ne peut plus changer de statut"));

        verify(ticketService).modifierStatut(1L, StatutTicket.IN_PROGRESS);
    }

    @Test
    void shouldReturnAllTickets() throws Exception {
        when(ticketService.listerTous()).thenReturn(List.of(
                new Ticket(1L, "Bug", Priorite.HIGH, StatutTicket.OPEN),
                new Ticket(2L, "Incident", Priorite.LOW, StatutTicket.OPEN)
        ));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(ticketService).listerTous();
    }
}
