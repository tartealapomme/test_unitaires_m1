package com.example.service;

import com.example.model.Adherent;
import com.example.model.Pret;
import com.example.repository.AdherentRepository;
import com.example.repository.OuvrageRepository;
import com.example.repository.PretRepository;

import java.time.LocalDate;
import java.util.UUID;

public class PretService {

    private static final int DUREE_PRET_JOURS = 14;

    private final PretRepository pretRepository;
    private final AdherentRepository adherentRepository;
    private final OuvrageRepository ouvrageRepository;

    public PretService(PretRepository pretRepository,
                       AdherentRepository adherentRepository,
                       OuvrageRepository ouvrageRepository) {
        this.pretRepository = pretRepository;
        this.adherentRepository = adherentRepository;
        this.ouvrageRepository = ouvrageRepository;
    }

    public Pret creerPret(String adherentId, String ouvrageIsbn) {
        Adherent adherent = adherentRepository.findById(adherentId)
                .orElseThrow(() -> new IllegalArgumentException("Adhérent introuvable"));
        ouvrageRepository.findByIsbn(ouvrageIsbn)
                .orElseThrow(() -> new IllegalArgumentException("Ouvrage introuvable"));

        LocalDate datePret = LocalDate.now();
        LocalDate dateRetourPrevue = datePret.plusDays(DUREE_PRET_JOURS);

        Pret pret = new Pret(
                UUID.randomUUID().toString(),
                adherent.getId(),
                ouvrageIsbn,
                datePret,
                dateRetourPrevue
        );
        return pretRepository.save(pret);
    }
}
