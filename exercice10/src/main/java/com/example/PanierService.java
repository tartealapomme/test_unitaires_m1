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
        Optional<Commande> commandeOpt = commandeRepository.findById(commandeId);
        if (commandeOpt.isEmpty() || produitRepository.findByReference(referenceProduit).isEmpty()) {
            return false;
        }
        Commande commande = commandeOpt.get();
        commande.definirQuantite(referenceProduit, commande.getQuantite(referenceProduit) + 1);
        commandeRepository.save(commande);
        return true;
    }

    public boolean supprimerProduit(String commandeId, String referenceProduit) {
        Optional<Commande> commandeOpt = commandeRepository.findById(commandeId);
        if (commandeOpt.isEmpty()) {
            return false;
        }
        Commande commande = commandeOpt.get();
        if (!commande.contientProduit(referenceProduit)) {
            return false;
        }
        commande.definirQuantite(referenceProduit, commande.getQuantite(referenceProduit) - 1);
        commandeRepository.save(commande);
        return true;
    }

    public Optional<String> validerCommande(String commandeId) {
        if (commandeRepository.findById(commandeId).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of("Commande validée");
    }
}
