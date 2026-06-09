package com.example;

import java.util.Optional;

public class CommandeService {

    private final ProduitRepository produitRepository;

    public CommandeService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public Optional<RecuCommande> passerCommande(String emailClient, String referenceProduit, int quantite, ProfilClient profil) {
        Optional<Produit> produitOpt = produitRepository.findByReference(referenceProduit);
        if (produitOpt.isEmpty()) {
            return Optional.empty();
        }
        Produit produit = produitOpt.get();
        if (quantite > produit.getStockDisponible()) {
            return Optional.empty();
        }
        double montantBrut = produit.getPrixUnitaire() * quantite;
        double tauxRemise = switch (profil) {
            case STANDARD -> 0.0;
            case PREMIUM -> 0.10;
            case VIP -> 0.20;
        };
        double montantTotal = montantBrut * (1 - tauxRemise);
        return Optional.of(new RecuCommande(
                referenceProduit,
                quantite,
                montantTotal,
                "Commande confirmée"
        ));
    }
}
