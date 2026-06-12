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

public class CompteSteps {

    private UtilisateurRepository utilisateurRepository;
    private AuthService authService;
    private Optional<String> resultat;

    @Given("aucun compte avec le nom d'utilisateur {string}")
    public void aucunCompte(String nomUtilisateur) {
        utilisateurRepository = mock(UtilisateurRepository.class);
        authService = new AuthService(utilisateurRepository);
        when(utilisateurRepository.existsByNomUtilisateur(nomUtilisateur)).thenReturn(false);
    }

    @Given("un compte existant avec le nom d'utilisateur {string}")
    public void compteExistant(String nomUtilisateur) {
        utilisateurRepository = mock(UtilisateurRepository.class);
        authService = new AuthService(utilisateurRepository);
        when(utilisateurRepository.existsByNomUtilisateur(nomUtilisateur)).thenReturn(true);
    }

    @Given("un compte existant {string} avec le mot de passe {string}")
    public void compteExistantAvecMotDePasse(String nomUtilisateur, String motDePasse) {
        utilisateurRepository = mock(UtilisateurRepository.class);
        authService = new AuthService(utilisateurRepository);
        when(utilisateurRepository.findByNomUtilisateur(nomUtilisateur))
                .thenReturn(Optional.of(new Utilisateur("user@mail.com", nomUtilisateur, motDePasse)));
    }

    @When("l'utilisateur crée un compte avec l'email {string}, le nom d'utilisateur {string} et le mot de passe {string}")
    public void creerCompte(String email, String nomUtilisateur, String motDePasse) {
        resultat = authService.creerCompte(email, nomUtilisateur, motDePasse);
    }

    @When("l'utilisateur se connecte avec le nom d'utilisateur {string} et le mot de passe {string}")
    public void connecter(String nomUtilisateur, String motDePasse) {
        resultat = authService.connecter(nomUtilisateur, motDePasse);
    }

    @Then("l'inscription est confirmée avec le message {string}")
    public void inscriptionConfirmee(String message) {
        assertTrue(resultat.isPresent());
        assertEquals(message, resultat.get());
    }

    @Then("l'inscription est refusée")
    public void inscriptionRefusee() {
        assertFalse(resultat.isPresent());
    }

    @Then("le dépôt utilisateur a été consulté pour {string}")
    public void depotUtilisateurConsulte(String nomUtilisateur) {
        verify(utilisateurRepository).existsByNomUtilisateur(nomUtilisateur);
    }

    @Then("la connexion est réussie")
    public void connexionReussie() {
        assertTrue(resultat.isPresent());
    }

    @Then("l'utilisateur est redirigé vers la page d'accueil")
    public void redirectionAccueil() {
        assertEquals("Page d'accueil", resultat.get());
    }

    @Then("la connexion échoue")
    public void connexionEchoue() {
        assertFalse(resultat.isPresent());
    }

    @Then("le message d'erreur est {string}")
    public void messageErreur(String message) {
        assertEquals(message, authService.getDernierMessageErreur());
    }
}
