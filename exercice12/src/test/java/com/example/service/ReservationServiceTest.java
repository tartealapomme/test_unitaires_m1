package com.example.service;

import com.example.exception.ReservationConflictException;
import com.example.exception.SalleNotFoundException;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.StatutReservation;
import com.example.repository.ReservationRepository;
import com.example.repository.SalleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final LocalDateTime DEBUT = LocalDateTime.of(2025, 6, 15, 10, 0);
    private static final LocalDateTime FIN = LocalDateTime.of(2025, 6, 15, 11, 0);

    @Mock
    private SalleRepository salleRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldCreateReservation_whenDataIsValid() {
        when(salleRepository.findById(1L)).thenReturn(Optional.of(new Salle(1L, "Salle A", 10)));
        when(reservationRepository.findBySalleId(1L)).thenReturn(List.of());
        when(reservationRepository.save(eq(1L), eq("Alice"), eq(DEBUT), eq(FIN), eq(StatutReservation.CONFIRMED)))
                .thenReturn(new Reservation(1L, 1L, "Alice", DEBUT, FIN, StatutReservation.CONFIRMED));

        Reservation result = reservationService.creer(1L, "Alice", DEBUT, FIN);

        assertEquals(StatutReservation.CONFIRMED, result.getStatut());
        assertEquals("Alice", result.getNomPersonne());
        verify(reservationRepository).save(1L, "Alice", DEBUT, FIN, StatutReservation.CONFIRMED);
    }

    @Test
    void shouldRejectReservation_whenRoomDoesNotExist() {
        when(salleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SalleNotFoundException.class,
                () -> reservationService.creer(99L, "Alice", DEBUT, FIN));

        verify(reservationRepository, never()).save(any(), any(), any(), any(), any());
    }

    @Test
    void shouldRejectReservation_whenTimeSlotIsInvalid() {
        when(salleRepository.findById(1L)).thenReturn(Optional.of(new Salle(1L, "Salle A", 10)));

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.creer(1L, "Alice", FIN, DEBUT));

        verify(reservationRepository, never()).save(any(), any(), any(), any(), any());
    }

    @Test
    void shouldRejectReservation_whenSlotOverlapsExistingReservation() {
        Reservation existante = new Reservation(1L, 1L, "Bob", DEBUT, FIN, StatutReservation.CONFIRMED);
        when(salleRepository.findById(1L)).thenReturn(Optional.of(new Salle(1L, "Salle A", 10)));
        when(reservationRepository.findBySalleId(1L)).thenReturn(List.of(existante));

        LocalDateTime nouveauDebut = LocalDateTime.of(2025, 6, 15, 10, 30);
        LocalDateTime nouvelleFin = LocalDateTime.of(2025, 6, 15, 11, 30);

        assertThrows(ReservationConflictException.class,
                () -> reservationService.creer(1L, "Alice", nouveauDebut, nouvelleFin));

        verify(reservationRepository, never()).save(any(), any(), any(), any(), any());
    }

    @Test
    void shouldCancelConfirmedReservation() {
        Reservation reservation = new Reservation(1L, 1L, "Alice", DEBUT, FIN, StatutReservation.CONFIRMED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.annuler(1L);

        assertEquals(StatutReservation.CANCELLED, result.getStatut());
    }

    @Test
    void shouldRejectCancellation_whenReservationAlreadyCancelled() {
        Reservation reservation = new Reservation(1L, 1L, "Alice", DEBUT, FIN, StatutReservation.CANCELLED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThrows(ReservationConflictException.class, () -> reservationService.annuler(1L));
    }
}
