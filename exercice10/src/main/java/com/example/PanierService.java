package com.example;

import java.util.Optional;

public class PanierService {

    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;

    public PanierService(CommandeRepository commandeRepository, ProduitRepository produitRepository) {
        this.commandeRepository = commandeRepository;
        this.produitRepository = produitRepository;
    }

    public boolean ajouterProduit(String commandeId, String referenceProduit) {
        throw new NotImplementedException();
    }

    public boolean supprimerProduit(String commandeId, String referenceProduit) {
        throw new NotImplementedException();
    }

    public Optional<String> validerCommande(String commandeId) {
        throw new NotImplementedException();
    }
}
