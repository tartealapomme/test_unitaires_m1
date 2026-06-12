package com.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PanierSteps {

    private CommandeRepository commandeRepository;
    private ProduitRepository produitRepository;
    private PanierService panierService;
    private Commande commandeCourante;
    private boolean operationReussie;
    private Optional<String> validationResultat;

    private void initPanierService() {
        if (commandeRepository == null) {
            commandeRepository = mock(CommandeRepository.class);
        }
        if (produitRepository == null) {
            produitRepository = mock(ProduitRepository.class);
        }
        panierService = new PanierService(commandeRepository, produitRepository);
    }

    @Given("une commande {string} vide")
    public void commandeVide(String commandeId) {
        initPanierService();
        commandeCourante = new Commande(commandeId);
        when(commandeRepository.findById(commandeId)).thenReturn(Optional.of(commandeCourante));
    }

    @Given("une commande {string} contenant {int} unité du produit {string}")
    public void commandeAvecProduitSingulier(String commandeId, int quantite, String reference) {
        commandeAvecProduit(commandeId, quantite, reference);
    }

    @Given("une commande {string} contenant {int} unités du produit {string}")
    public void commandeAvecProduit(String commandeId, int quantite, String reference) {
        initPanierService();
        commandeCourante = new Commande(commandeId);
        commandeCourante.definirQuantite(reference, quantite);
        when(commandeRepository.findById(commandeId)).thenReturn(Optional.of(commandeCourante));
    }

    @Given("aucune commande avec l'identifiant {string}")
    public void aucuneCommande(String commandeId) {
        initPanierService();
        when(commandeRepository.findById(commandeId)).thenReturn(Optional.empty());
    }

    @Given("un produit {string} nommé {string} au prix de {double} euros")
    public void unProduit(String reference, String nom, double prix) {
        initPanierService();
        when(produitRepository.findByReference(reference))
                .thenReturn(Optional.of(new Produit(reference, nom, prix, "Informatique")));
    }

    @When("l'utilisateur ajoute le produit {string} à la commande {string}")
    public void ajouterProduit(String reference, String commandeId) {
        operationReussie = panierService.ajouterProduit(commandeId, reference);
    }

    @When("l'utilisateur supprime le produit {string} de la commande {string}")
    public void supprimerProduit(String reference, String commandeId) {
        operationReussie = panierService.supprimerProduit(commandeId, reference);
    }

    @When("l'utilisateur valide la commande {string}")
    public void validerCommande(String commandeId) {
        validationResultat = panierService.validerCommande(commandeId);
    }

    @Then("l'ajout est confirmé")
    public void ajoutConfirme() {
        assertTrue(operationReussie);
    }

    @Then("l'ajout est refusé")
    public void ajoutRefuse() {
        assertFalse(operationReussie);
    }

    @Then("la suppression est confirmée")
    public void suppressionConfirmee() {
        assertTrue(operationReussie);
    }

    @Then("la suppression est refusée")
    public void suppressionRefusee() {
        assertFalse(operationReussie);
    }

    @Then("la quantité du produit {string} dans la commande {string} est de {int}")
    public void quantiteProduit(String reference, String commandeId, int quantiteAttendue) {
        Commande commande = commandeRepository.findById(commandeId).orElseThrow();
        assertEquals(quantiteAttendue, commande.getQuantite(reference));
    }

    @Then("le produit {string} n'est plus dans la commande {string}")
    public void produitAbsent(String reference, String commandeId) {
        Commande commande = commandeRepository.findById(commandeId).orElseThrow();
        assertFalse(commande.contientProduit(reference));
    }

    @Then("la validation est confirmée avec le message {string}")
    public void validationConfirmee(String message) {
        assertTrue(validationResultat.isPresent());
        assertEquals(message, validationResultat.get());
    }

    @Then("la validation est refusée")
    public void validationRefusee() {
        assertFalse(validationResultat.isPresent());
    }
}
