package com.example;

import java.util.List;
import java.util.Optional;

public interface ProduitRepository {
    Optional<Produit> findByReference(String reference);

    List<Produit> findByMotCle(String motCle);

    List<Produit> findByCategorie(String categorie);

    List<Produit> findByPrixMaximum(double prixMaximum);
}
