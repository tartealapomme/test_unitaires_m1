package com.example;

import java.util.List;

public class CatalogueService {

    private final ProduitRepository produitRepository;

    public CatalogueService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> rechercherParMotCle(String motCle) {
        return produitRepository.findByMotCle(motCle);
    }

    public List<Produit> rechercherParPrixMaximum(double prixMaximum) {
        return produitRepository.findByPrixMaximum(prixMaximum);
    }

    public List<Produit> trouverParCategorie(String categorie) {
        return produitRepository.findByCategorie(categorie);
    }
}
