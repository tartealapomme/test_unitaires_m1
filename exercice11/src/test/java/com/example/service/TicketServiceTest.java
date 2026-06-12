package com.example.service;

import com.example.exception.InvalidStatusTransitionException;
import com.example.exception.TicketNotFoundException;
import com.example.model.Priorite;
import com.example.model.StatutTicket;
import com.example.model.Ticket;
import com.example.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void shouldCreateTicket_whenDataIsValid() {
        when(ticketRepository.save("Bug critique", Priorite.HIGH, StatutTicket.OPEN))
                .thenReturn(new Ticket(1L, "Bug critique", Priorite.HIGH, StatutTicket.OPEN));

        Ticket result = ticketService.creer("Bug critique", Priorite.HIGH);

        assertEquals("Bug critique", result.getTitre());
        assertEquals(Priorite.HIGH, result.getPriorite());
        verify(ticketRepository).save("Bug critique", Priorite.HIGH, StatutTicket.OPEN);
    }

    @Test
    void shouldSetDefaultStatusToOpen_whenCreatingTicket() {
        when(ticketRepository.save("Incident", Priorite.MEDIUM, StatutTicket.OPEN))
                .thenReturn(new Ticket(1L, "Incident", Priorite.MEDIUM, StatutTicket.OPEN));

        Ticket result = ticketService.creer("Incident", Priorite.MEDIUM);

        assertEquals(StatutTicket.OPEN, result.getStatut());
    }

    @Test
    void shouldTrimTitle_whenCreatingTicket() {
        when(ticketRepository.save("Bug", Priorite.LOW, StatutTicket.OPEN))
                .thenReturn(new Ticket(1L, "Bug", Priorite.LOW, StatutTicket.OPEN));

        Ticket result = ticketService.creer("  Bug  ", Priorite.LOW);

        assertEquals("Bug", result.getTitre());
        verify(ticketRepository).save("Bug", Priorite.LOW, StatutTicket.OPEN);
    }

    @Test
    void shouldThrowException_whenTitleIsTooShort() {
        assertThrows(IllegalArgumentException.class, () -> ticketService.creer("ab", Priorite.LOW));
        verify(ticketRepository, never()).save(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldReturnTicket_whenTicketExists() {
        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(new Ticket(1L, "Bug", Priorite.HIGH, StatutTicket.OPEN)));

        Ticket result = ticketService.trouverParId(1L);

        assertEquals(1L, result.getId());
        verify(ticketRepository).findById(1L);
    }

    @Test
    void shouldThrowNotFoundException_whenTicketDoesNotExist() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.trouverParId(99L));
        verify(ticketRepository).findById(99L);
    }

    @Test
    void shouldAllowTransitionFromOpenToInProgress() {
        Ticket ticket = new Ticket(1L, "Bug", Priorite.HIGH, StatutTicket.OPEN);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.modifierStatut(1L, StatutTicket.IN_PROGRESS);

        assertEquals(StatutTicket.IN_PROGRESS, result.getStatut());
    }

    @Test
    void shouldAllowTransitionFromOpenToResolved() {
        Ticket ticket = new Ticket(1L, "Bug", Priorite.HIGH, StatutTicket.OPEN);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.modifierStatut(1L, StatutTicket.RESOLVED);

        assertEquals(StatutTicket.RESOLVED, result.getStatut());
    }

    @Test
    void shouldAllowTransitionFromInProgressToResolved() {
        Ticket ticket = new Ticket(1L, "Bug", Priorite.HIGH, StatutTicket.IN_PROGRESS);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.modifierStatut(1L, StatutTicket.RESOLVED);

        assertEquals(StatutTicket.RESOLVED, result.getStatut());
    }

    @Test
    void shouldRejectTransition_whenTicketIsResolved() {
        Ticket ticket = new Ticket(1L, "Bug", Priorite.HIGH, StatutTicket.RESOLVED);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(InvalidStatusTransitionException.class,
                () -> ticketService.modifierStatut(1L, StatutTicket.IN_PROGRESS));
    }

    @Test
    void shouldRejectInvalidTransition_fromInProgressToOpen() {
        Ticket ticket = new Ticket(1L, "Bug", Priorite.HIGH, StatutTicket.IN_PROGRESS);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(InvalidStatusTransitionException.class,
                () -> ticketService.modifierStatut(1L, StatutTicket.OPEN));
    }
}
