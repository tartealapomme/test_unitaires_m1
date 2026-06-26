package com.example.service;

import com.example.exception.AdherentSuspenduException;
import com.example.exception.OuvrageDisponibleException;
import com.example.model.Adherent;
import com.example.model.Pret;
import com.example.model.Reservation;
import com.example.repository.AdherentRepository;
import com.example.repository.PretRepository;
import com.example.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PretRepository pretRepository;

    @Mock
    private AdherentRepository adherentRepository;

    @Mock
    private PretService pretService;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                reservationRepository, pretRepository, adherentRepository, pretService);
    }

    @Test
    void shouldReserveBook_whenBookIsUnavailable() {
        when(adherentRepository.findById("A1"))
                .thenReturn(Optional.of(new Adherent("A1", "Alice")));
        when(pretService.estOuvrageDisponible("978-1")).thenReturn(false);
        when(reservationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation reservation = reservationService.reserver("A1", "978-1");

        assertThat(reservation.getAdherentId()).isEqualTo("A1");
        assertThat(reservation.getOuvrageIsbn()).isEqualTo("978-1");
    }

    @Test
    void shouldRejectReservation_whenBookIsAvailable() {
        when(adherentRepository.findById("A1"))
                .thenReturn(Optional.of(new Adherent("A1", "Alice")));
        when(pretService.estOuvrageDisponible("978-1")).thenReturn(true);

        assertThatThrownBy(() -> reservationService.reserver("A1", "978-1"))
                .isInstanceOf(OuvrageDisponibleException.class);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldRejectReservation_whenAdherentIsSuspended() {
        Adherent adherent = new Adherent("A1", "Alice");
        adherent.setSuspendu(true);
        when(adherentRepository.findById("A1")).thenReturn(Optional.of(adherent));

        assertThatThrownBy(() -> reservationService.reserver("A1", "978-1"))
                .isInstanceOf(AdherentSuspenduException.class);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldAssignBookToFirstReservation_whenBookIsReturned() {
        Pret pretEnCours = new Pret("P1", "A2", "978-1",
                LocalDate.now(), LocalDate.now().plusDays(21));
        Reservation reservation = new Reservation("R1", "A1", "978-1", LocalDateTime.now());

        when(pretRepository.findEnCoursByOuvrageIsbn("978-1")).thenReturn(List.of(pretEnCours));
        when(reservationRepository.findByOuvrageIsbn("978-1")).thenReturn(List.of(reservation));

        reservationService.traiterRestitution("978-1", LocalDate.now());

        verify(pretService).retournerPret("P1", LocalDate.now());
        verify(pretService).creerPret("A1", "978-1");
        verify(reservationRepository).deleteById("R1");
    }
}
