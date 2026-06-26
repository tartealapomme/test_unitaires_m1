package com.example.service;

import com.example.exception.AdherentSuspenduException;
import com.example.exception.OuvrageDisponibleException;
import com.example.model.Adherent;
import com.example.model.Reservation;
import com.example.repository.AdherentRepository;
import com.example.repository.PretRepository;
import com.example.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PretRepository pretRepository;
    private final AdherentRepository adherentRepository;
    private final PretService pretService;

    public ReservationService(ReservationRepository reservationRepository,
                              PretRepository pretRepository,
                              AdherentRepository adherentRepository,
                              PretService pretService) {
        this.reservationRepository = reservationRepository;
        this.pretRepository = pretRepository;
        this.adherentRepository = adherentRepository;
        this.pretService = pretService;
    }

    public Reservation reserver(String adherentId, String ouvrageIsbn) {
        Adherent adherent = adherentRepository.findById(adherentId)
                .orElseThrow(() -> new IllegalArgumentException("Adhérent introuvable"));
        if (adherent.isSuspendu()) {
            throw new AdherentSuspenduException(adherentId);
        }
        if (pretService.estOuvrageDisponible(ouvrageIsbn)) {
            throw new OuvrageDisponibleException(ouvrageIsbn);
        }

        Reservation reservation = new Reservation(
                UUID.randomUUID().toString(),
                adherentId,
                ouvrageIsbn,
                LocalDateTime.now()
        );
        return reservationRepository.save(reservation);
    }

    public List<Reservation> listerReservations(String ouvrageIsbn) {
        return reservationRepository.findByOuvrageIsbn(ouvrageIsbn);
    }

    public void traiterRestitution(String ouvrageIsbn, LocalDate dateRetour) {
        pretRepository.findEnCoursByOuvrageIsbn(ouvrageIsbn).stream()
                .findFirst()
                .ifPresent(pret -> pretService.retournerPret(pret.getId(), dateRetour));

        List<Reservation> reservations = reservationRepository.findByOuvrageIsbn(ouvrageIsbn);
        if (!reservations.isEmpty()) {
            Reservation prochaine = reservations.get(0);
            pretService.creerPret(prochaine.getAdherentId(), ouvrageIsbn);
            reservationRepository.deleteById(prochaine.getId());
        }
    }
}
