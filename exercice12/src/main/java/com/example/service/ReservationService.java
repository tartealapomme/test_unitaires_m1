package com.example.service;

import com.example.exception.ReservationConflictException;
import com.example.exception.ReservationNotFoundException;
import com.example.exception.SalleNotFoundException;
import com.example.model.Reservation;
import com.example.model.StatutReservation;
import com.example.repository.ReservationRepository;
import com.example.repository.SalleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(SalleRepository salleRepository, ReservationRepository reservationRepository) {
        this.salleRepository = salleRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation creer(Long salleId, String nomPersonne, LocalDateTime debut, LocalDateTime fin) {
        if (salleRepository.findById(salleId).isEmpty()) {
            throw new SalleNotFoundException(salleId);
        }
        if (nomPersonne == null || nomPersonne.isBlank()) {
            throw new IllegalArgumentException("Le nom de la personne est obligatoire");
        }
        if (debut == null || fin == null || !fin.isAfter(debut)) {
            throw new IllegalArgumentException("La date de fin doit être strictement après la date de début");
        }
        if (aUnChevauchement(salleId, debut, fin)) {
            throw new ReservationConflictException("Le créneau chevauche une réservation existante");
        }
        return reservationRepository.save(salleId, nomPersonne.trim(), debut, fin, StatutReservation.CONFIRMED);
    }

    public Reservation trouverParId(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public Reservation annuler(Long id) {
        Reservation reservation = trouverParId(id);
        if (reservation.getStatut() == StatutReservation.CANCELLED) {
            throw new ReservationConflictException("La réservation est déjà annulée");
        }
        reservation.setStatut(StatutReservation.CANCELLED);
        return reservation;
    }

    private boolean aUnChevauchement(Long salleId, LocalDateTime debut, LocalDateTime fin) {
        return reservationRepository.findBySalleId(salleId).stream()
                .filter(reservation -> reservation.getStatut() == StatutReservation.CONFIRMED)
                .anyMatch(reservation -> chevauche(debut, fin, reservation.getDebut(), reservation.getFin()));
    }

    private boolean chevauche(LocalDateTime debut1, LocalDateTime fin1,
                              LocalDateTime debut2, LocalDateTime fin2) {
        return debut1.isBefore(fin2) && debut2.isBefore(fin1);
    }
}
