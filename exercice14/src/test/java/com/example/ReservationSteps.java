package com.example;

import com.example.exception.AdherentSuspenduException;
import com.example.model.Adherent;
import com.example.model.Ouvrage;
import com.example.model.Pret;
import com.example.model.Reservation;
import com.example.repository.memory.InMemoryAdherentRepository;
import com.example.repository.memory.InMemoryOuvrageRepository;
import com.example.repository.memory.InMemoryPretRepository;
import com.example.repository.memory.InMemoryReservationRepository;
import com.example.service.PretService;
import com.example.service.ReservationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationSteps {

    private InMemoryAdherentRepository adherentRepository;
    private InMemoryOuvrageRepository ouvrageRepository;
    private InMemoryPretRepository pretRepository;
    private InMemoryReservationRepository reservationRepository;
    private PretService pretService;
    private ReservationService reservationService;

    private boolean reservationAcceptee;
    private boolean empruntAccepte;
    private Exception derniereErreur;

    private void init() {
        adherentRepository = new InMemoryAdherentRepository();
        ouvrageRepository = new InMemoryOuvrageRepository();
        pretRepository = new InMemoryPretRepository();
        reservationRepository = new InMemoryReservationRepository();
        pretService = new PretService(pretRepository, adherentRepository, ouvrageRepository);
        reservationService = new ReservationService(
                reservationRepository, pretRepository, adherentRepository, pretService);
        reservationAcceptee = false;
        empruntAccepte = false;
        derniereErreur = null;
    }

    @Given("un ouvrage {string} intitulé {string} existe")
    public void ouvrageExiste(String isbn, String titre) {
        init();
        ouvrageRepository.save(new Ouvrage(isbn, titre));
    }

    @Given("l'adhérent {string} nommé {string} existe")
    public void adherentExiste(String id, String nom) {
        adherentRepository.save(new Adherent(id, nom));
    }

    @Given("l'adhérent {string} nommé {string} est suspendu")
    public void adherentSuspendu(String id, String nom) {
        init();
        Adherent adherent = new Adherent(id, nom);
        adherent.setSuspendu(true);
        adherentRepository.save(adherent);
    }

    @Given("l'ouvrage {string} est emprunté par {string}")
    public void ouvrageEmprunte(String isbn, String adherentId) {
        Pret pret = new Pret(
                UUID.randomUUID().toString(),
                adherentId,
                isbn,
                LocalDate.now(),
                LocalDate.now().plusDays(21)
        );
        pretRepository.save(pret);
    }

    @Given("l'adhérent {string} a réservé l'ouvrage {string}")
    public void reservationExiste(String adherentId, String isbn) {
        reservationService.reserver(adherentId, isbn);
    }

    @When("l'adhérent {string} réserve l'ouvrage {string}")
    public void reserver(String adherentId, String isbn) {
        try {
            reservationService.reserver(adherentId, isbn);
            reservationAcceptee = true;
        } catch (Exception e) {
            derniereErreur = e;
            reservationAcceptee = false;
        }
    }

    @When("l'ouvrage {string} est restitué")
    public void restituer(String isbn) {
        reservationService.traiterRestitution(isbn, LocalDate.now());
    }

    @When("l'adhérent {string} emprunte l'ouvrage {string}")
    public void emprunter(String adherentId, String isbn) {
        try {
            pretService.creerPret(adherentId, isbn);
            empruntAccepte = true;
        } catch (Exception e) {
            derniereErreur = e;
            empruntAccepte = false;
        }
    }

    @Then("la réservation est acceptée")
    public void reservationAcceptee() {
        assertThat(reservationAcceptee).isTrue();
    }

    @Then("la réservation est refusée")
    public void reservationRefusee() {
        assertThat(reservationAcceptee).isFalse();
        assertThat(derniereErreur).isInstanceOf(AdherentSuspenduException.class);
    }

    @Then("il y a {int} réservations sur l'ouvrage {string}")
    public void nombreReservations(int nombre, String isbn) {
        assertThat(reservationService.listerReservations(isbn)).hasSize(nombre);
    }

    @Then("l'ouvrage {string} devrait être emprunté par {string}")
    public void ouvrageDevraitEtreEmpruntePar(String isbn, String adherentId) {
        assertThat(pretRepository.findEnCoursByOuvrageIsbn(isbn))
                .extracting(Pret::getAdherentId)
                .containsExactly(adherentId);
    }

    @Then("l'emprunt est refusé")
    public void empruntRefuse() {
        assertThat(empruntAccepte).isFalse();
        assertThat(derniereErreur).isInstanceOf(AdherentSuspenduException.class);
    }
}
