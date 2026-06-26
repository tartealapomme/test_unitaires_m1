package com.example.bdd;

import com.example.model.Compte;
import com.example.repository.CompteRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CompteStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompteRepository compteRepository;

    private ResultActions lastResult;

    @Given("aucun compte n'existe dans l'API")
    public void aucunCompte() {
        compteRepository.deleteAll();
    }

    @Given("un compte {string} appartenant à {string} existe avec un solde de {int}")
    public void compteExiste(String numero, String titulaire, int solde) {
        Compte compte = compteRepository.save(numero, titulaire);
        compte.setSolde(BigDecimal.valueOf(solde));
    }

    @When("je crée un compte avec le numéro {string} pour le titulaire {string}")
    public void creerCompte(String numero, String titulaire) throws Exception {
        lastResult = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "numero": "%s",
                          "titulaire": "%s"
                        }
                        """.formatted(numero, titulaire)));
    }

    @When("je dépose {int} sur le compte {string}")
    public void deposer(int montant, String numero) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/" + numero + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"montant\": " + montant + "}"));
    }

    @When("je retire {int} sur le compte {string}")
    public void retirer(int montant, String numero) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/" + numero + "/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"montant\": " + montant + "}"));
    }

    @When("je vire {int} du compte {string} vers le compte {string}")
    public void virer(int montant, String source, String destination) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "compteSource": "%s",
                          "compteDestination": "%s",
                          "montant": %d
                        }
                        """.formatted(source, destination, montant)));
    }

    @Then("la réponse HTTP doit être {int}")
    public void reponseHttp(int status) throws Exception {
        lastResult.andExpect(status().is(status));
    }

    @Then("la réponse contient le numéro {string}")
    public void reponseContientNumero(String numero) throws Exception {
        lastResult.andExpect(jsonPath("$.numero").value(numero));
    }

    @Then("la réponse contient le titulaire {string}")
    public void reponseContientTitulaire(String titulaire) throws Exception {
        lastResult.andExpect(jsonPath("$.titulaire").value(titulaire));
    }

    @Then("la réponse contient le solde {int}")
    public void reponseContientSolde(int solde) throws Exception {
        lastResult.andExpect(jsonPath("$.solde").value(solde));
    }

    @Then("la réponse contient un message d'erreur")
    public void reponseContientErreur() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }

    @Then("le compte {string} a un solde de {int}")
    public void compteASolde(String numero, int solde) throws Exception {
        mockMvc.perform(get("/accounts/" + numero))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(solde));
    }
}
