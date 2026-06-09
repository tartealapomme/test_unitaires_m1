package com.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservationSteps {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private SalleRepository salleRepository;
    private ReservationRepository reservationRepository;
    private NotificationService notificationService;
    private ReservationSalleService reservationSalleService;
    private boolean resultat;

    private void initMocks() {
        salleRepository = mock(SalleRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        notificationService = mock(NotificationService.class);
        reservationSalleService = new ReservationSalleService(
                salleRepository, reservationRepository, notificationService);
    }

    @Given("une salle {string} nommée {string} avec une capacité de {int}")
    public void uneSalle(String code, String nom, int capacite) {
        initMocks();
        when(salleRepository.findByCode(code)).thenReturn(java.util.Optional.of(new Salle(code, nom, capacite)));
        when(reservationRepository.findByCodeSalle(code)).thenReturn(new ArrayList<>());
    }

    @Given("aucune salle avec le code {string}")
    public void aucuneSalle(String code) {
        initMocks();
        when(salleRepository.findByCode(code)).thenReturn(java.util.Optional.empty());
        when(reservationRepository.findByCodeSalle(code)).thenReturn(new ArrayList<>());
    }

    @Given("une réservation existante sur la salle {string} du {string} au {string}")
    public void reservationExistante(String code, String debut, String fin) {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(
                "autre@mail.com", code, 1, parseDate(debut), parseDate(fin)));
        when(reservationRepository.findByCodeSalle(code)).thenReturn(reservations);
    }

    @When("l'utilisateur {string} réserve la salle {string} pour {int} participants du {string} au {string}")
    public void reserver(String email, String code, int participants, String debut, String fin) {
        resultat = reservationSalleService.reserver(
                email, code, participants, parseDate(debut), parseDate(fin));
    }

    @Then("la réservation est acceptée")
    public void reservationAcceptee() {
        assertTrue(resultat);
    }

    @Then("la réservation est refusée")
    public void reservationRefusee() {
        assertFalse(resultat);
    }

    @Then("une confirmation est envoyée à {string}")
    public void confirmationEnvoyee(String email) {
        verify(notificationService).envoyerConfirmation(email, "Réservation confirmée");
    }

    @Then("aucune confirmation n'est envoyée")
    public void aucuneConfirmation() {
        verify(notificationService, never()).envoyerConfirmation(anyString(), anyString());
    }

    @Then("le dépôt salle a été consulté pour {string}")
    public void depotSalleConsulte(String code) {
        verify(salleRepository).findByCode(code);
    }

    private LocalDateTime parseDate(String value) {
        return LocalDateTime.parse(value, FORMAT);
    }
}
