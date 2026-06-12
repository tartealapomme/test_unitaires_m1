package com.example.bdd;

import com.example.model.Priorite;
import com.example.model.StatutTicket;
import com.example.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TicketStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;
    private Long createdTicketId;

    @Given("aucun ticket n'existe dans l'API")
    public void aucunTicket() {
        ticketRepository.deleteAll();
        createdTicketId = null;
    }

    @Given("un ticket existe avec le titre {string} et la priorité {string}")
    public void ticketExiste(String titre, String priorite) {
        var ticket = ticketRepository.save(titre, Priorite.valueOf(priorite), StatutTicket.OPEN);
        createdTicketId = ticket.getId();
    }

    @Given("un ticket résolu existe avec le titre {string} et la priorité {string}")
    public void ticketResoluExiste(String titre, String priorite) {
        var ticket = ticketRepository.save(titre, Priorite.valueOf(priorite), StatutTicket.RESOLVED);
        createdTicketId = ticket.getId();
    }

    @When("je crée un ticket avec le titre {string} et la priorité {string}")
    public void creerTicket(String titre, String priorite) throws Exception {
        lastResult = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titre\":\"" + titre + "\",\"priorite\":\"" + priorite + "\"}"));

        var responseBody = lastResult.andReturn().getResponse().getContentAsString();
        if (!responseBody.isBlank() && lastResult.andReturn().getResponse().getStatus() == 201) {
            createdTicketId = objectMapper.readTree(responseBody).get("id").asLong();
        }
    }

    @When("je modifie le statut du ticket créé vers {string}")
    public void modifierStatut(String statut) throws Exception {
        lastResult = mockMvc.perform(patch("/api/tickets/" + createdTicketId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"statut\":\"" + statut + "\"}"));
    }

    @When("je consulte le ticket avec l'identifiant {int}")
    public void consulterTicket(int id) throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets/" + id));
    }

    @Then("la réponse HTTP doit être {int}")
    public void reponseHttp(int status) throws Exception {
        lastResult.andExpect(status().is(status));
    }

    @Then("la réponse contient le titre {string}")
    public void reponseContientTitre(String titre) throws Exception {
        lastResult.andExpect(jsonPath("$.titre").value(titre));
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
