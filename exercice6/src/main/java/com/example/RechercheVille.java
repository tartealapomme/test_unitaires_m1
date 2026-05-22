package com.example;

import java.util.List;

public class RechercheVille {

    private final SourceVilles sourceVilles;

    public RechercheVille(SourceVilles sourceVilles) {
        this.sourceVilles = sourceVilles;
    }

    public List<String> rechercher(String mot) {
        if ("*".equals(mot)) {
            return sourceVilles.getVilles();
        }
        if (mot == null || mot.length() < 2) {
            throw new NotFoundException("Search text must contain at least 2 characters");
        }
        String recherche = mot.toLowerCase();
        return sourceVilles.getVilles().stream()
                .filter(ville -> ville.toLowerCase().contains(recherche))
                .toList();
    }
}
