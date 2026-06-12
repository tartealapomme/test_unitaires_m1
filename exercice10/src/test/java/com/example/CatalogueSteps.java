package com.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CatalogueSteps {

    private ProduitRepository produitRepository;
    private CatalogueService catalogueService;
    private List<Produit> resultats;

    @Given("les produits suivants sont disponibles:")
    public void produitsDisponibles(DataTable table) {
        produitRepository = mock(ProduitRepository.class);
        catalogueService = new CatalogueService(produitRepository);
        List<Produit> produits = new ArrayList<>();
        for (Map<String, String> ligne : table.asMaps()) {
            produits.add(new Produit(
                    ligne.get("reference"),
                    ligne.get("nom"),
                    Double.parseDouble(ligne.get("prix")),
                    ligne.get("categorie")
            ));
        }
        when(produitRepository.findByMotCle(anyString())).thenAnswer(invocation -> {
            String motCle = invocation.getArgument(0, String.class).toLowerCase(Locale.ROOT);
            return produits.stream()
                    .filter(produit -> produit.getNom().toLowerCase(Locale.ROOT).contains(motCle))
                    .toList();
        });
        when(produitRepository.findByPrixMaximum(anyDouble())).thenAnswer(invocation -> {
            double prixMaximum = invocation.getArgument(0, Double.class);
            return produits.stream()
                    .filter(produit -> produit.getPrix() <= prixMaximum)
                    .toList();
        });
        when(produitRepository.findByCategorie(anyString())).thenAnswer(invocation -> {
            String categorie = invocation.getArgument(0, String.class);
            return produits.stream()
                    .filter(produit -> produit.getCategorie().equals(categorie))
                    .toList();
        });
    }

    @When("l'utilisateur recherche le mot-clé {string}")
    public void rechercherMotCle(String motCle) {
        resultats = catalogueService.rechercherParMotCle(motCle);
    }

    @When("l'utilisateur recherche des produits avec un prix maximum de {double} euros")
    public void rechercherPrixMaximum(double prixMaximum) {
        resultats = catalogueService.rechercherParPrixMaximum(prixMaximum);
    }

    @When("l'utilisateur sélectionne la catégorie {string}")
    public void selectionnerCategorie(String categorie) {
        resultats = catalogueService.trouverParCategorie(categorie);
    }

    @Then("la recherche retourne {int} produit")
    public void rechercheRetourneProduits(int nombre) {
        assertEquals(nombre, resultats.size());
    }

    @Then("la catégorie retourne {int} produit")
    public void categorieRetourneProduits(int nombre) {
        assertEquals(nombre, resultats.size());
    }

    @Then("le produit {string} est dans les résultats")
    public void produitDansResultats(String reference) {
        assertTrue(resultats.stream().anyMatch(produit -> produit.getReference().equals(reference)));
    }
}
