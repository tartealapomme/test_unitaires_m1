package com.example;

import java.util.LinkedHashMap;
import java.util.Map;

public class Commande {
    private final String id;
    private final Map<String, Integer> lignes;

    public Commande(String id) {
        this.id = id;
        this.lignes = new LinkedHashMap<>();
    }

    public String getId() {
        return id;
    }

    public Map<String, Integer> getLignes() {
        return lignes;
    }

    public int getQuantite(String referenceProduit) {
        return lignes.getOrDefault(referenceProduit, 0);
    }

    public boolean contientProduit(String referenceProduit) {
        return lignes.containsKey(referenceProduit);
    }

    public void definirQuantite(String referenceProduit, int quantite) {
        if (quantite <= 0) {
            lignes.remove(referenceProduit);
            return;
        }
        lignes.put(referenceProduit, quantite);
    }
}
