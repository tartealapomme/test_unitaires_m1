package com.example.bdd;

import com.example.model.StatutReservation;
import com.example.repository.ReservationRepository;
import com.example.repository.SalleRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ResultActions lastResult;

    @Given("aucune donnée n'existe dans l'API")
    public void aucuneDonnee() {
        reservationRepository.deleteAll();
        salleRepository.deleteAll();
    }

    @Given("une salle {string} avec une capacité de {int} existe")
    public void salleExiste(String nom, int capacite) {
        salleRepository.save(nom, capacite);
    }

    @Given("une réservation confirmée existe sur la salle {long} du {string} au {string}")
    public void reservationConfirmeeExiste(Long salleId, String debut, String fin) {
        reservationRepository.save(
                salleId,
                "Existant",
                LocalDateTime.parse(debut),
                LocalDateTime.parse(fin),
                StatutReservation.CONFIRMED
        );
    }

    @When("je réserve la salle {long} pour {string} du {string} au {string}")
    public void reserver(Long salleId, String nomPersonne, String debut, String fin) throws Exception {
        lastResult = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "salleId": %d,
                          "nomPersonne": "%s",
                          "debut": "%s",
                          "fin": "%s"
                        }
                        """.formatted(salleId, nomPersonne, debut, fin)));
    }

    @Then("la réponse HTTP doit être {int}")
    public void reponseHttp(int status) throws Exception {
        lastResult.andExpect(status().is(status));
    }

    @Then("la réponse contient le statut {string}")
    public void reponseContientStatut(String statut) throws Exception {
        lastResult.andExpect(jsonPath("$.statut").value(statut));
    }

    @Then("la réponse contient un message d'erreur")
    public void reponseContientErreur() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }
}
