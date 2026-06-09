package com.example;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationSalleService {

    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public ReservationSalleService(SalleRepository salleRepository,
                                   ReservationRepository reservationRepository,
                                   NotificationService notificationService) {
        this.salleRepository = salleRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public boolean reserver(String emailUtilisateur, String codeSalle, int nombreParticipants,
                            LocalDateTime dateDebut, LocalDateTime dateFin) {
        var salleOpt = salleRepository.findByCode(codeSalle);
        if (salleOpt.isEmpty()) {
            return false;
        }
        Salle salle = salleOpt.get();
        if (nombreParticipants > salle.getCapaciteMax()) {
            return false;
        }
        if (!dateFin.isAfter(dateDebut)) {
            return false;
        }
        List<Reservation> reservations = reservationRepository.findByCodeSalle(codeSalle);
        for (Reservation reservation : reservations) {
            if (chevauche(dateDebut, dateFin, reservation.getDateDebut(), reservation.getDateFin())) {
                return false;
            }
        }
        notificationService.envoyerConfirmation(emailUtilisateur, "Réservation confirmée");
        return true;
    }

    private boolean chevauche(LocalDateTime debut1, LocalDateTime fin1,
                              LocalDateTime debut2, LocalDateTime fin2) {
        return debut1.isBefore(fin2) && debut2.isBefore(fin1);
    }
}
