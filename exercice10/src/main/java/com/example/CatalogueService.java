package com.example;

import java.util.List;

public class CatalogueService {

    private final ProduitRepository produitRepository;

    public CatalogueService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> rechercherParMotCle(String motCle) {
        throw new NotImplementedException();
    }

    public List<Produit> rechercherParPrixMaximum(double prixMaximum) {
        throw new NotImplementedException();
    }

    public List<Produit> trouverParCategorie(String categorie) {
        throw new NotImplementedException();
    }
}
