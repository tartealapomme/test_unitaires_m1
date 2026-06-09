package com.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandeSteps {

    private ProduitRepository produitRepository;
    private CommandeService commandeService;
    private String emailClient;
    private ProfilClient profilClient;
    private Optional<RecuCommande> resultat;

    @Given("un produit {string} nommé {string} au prix de {double} euros avec un stock de {int}")
    public void unProduit(String reference, String nom, double prix, int stock) {
        produitRepository = mock(ProduitRepository.class);
        commandeService = new CommandeService(produitRepository);
        when(produitRepository.findByReference(reference))
                .thenReturn(Optional.of(new Produit(reference, nom, prix, stock)));
    }

    @Given("aucun produit avec la référence {string}")
    public void aucunProduit(String reference) {
        produitRepository = mock(ProduitRepository.class);
        commandeService = new CommandeService(produitRepository);
        when(produitRepository.findByReference(reference)).thenReturn(Optional.empty());
    }

    @Given("un client {string} avec l'email {string}")
    public void unClient(String profil, String email) {
        profilClient = ProfilClient.valueOf(profil);
        emailClient = email;
    }

    @When("le client passe une commande de {int} unités du produit {string}")
    public void passerCommande(int quantite, String reference) {
        resultat = commandeService.passerCommande(emailClient, reference, quantite, profilClient);
    }

    @Then("la commande est acceptée")
    public void commandeAcceptee() {
        assertTrue(resultat.isPresent());
    }

    @Then("la commande est refusée")
    public void commandeRefusee() {
        assertFalse(resultat.isPresent());
    }

    @Then("le montant total est de {double} euros")
    public void montantTotal(double montantAttendu) {
        assertEquals(montantAttendu, resultat.get().getMontantTotal(), 0.001);
    }

    @Then("la quantité commandée est de {int}")
    public void quantiteCommandee(int quantiteAttendue) {
        assertEquals(quantiteAttendue, resultat.get().getQuantite());
    }

    @Then("la référence produit du reçu est {string}")
    public void referenceProduit(String referenceAttendue) {
        assertEquals(referenceAttendue, resultat.get().getReferenceProduit());
    }

    @Then("le message de confirmation est {string}")
    public void messageConfirmation(String messageAttendu) {
        assertEquals(messageAttendu, resultat.get().getMessageConfirmation());
    }

    @Then("le dépôt produit a été consulté pour {string}")
    public void depotConsulte(String reference) {
        verify(produitRepository).findByReference(reference);
    }
}
