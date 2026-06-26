package com.example.service;

import com.example.exception.AdherentSuspenduException;
import com.example.exception.OuvrageIndisponibleException;
import com.example.model.Adherent;
import com.example.model.Pret;
import com.example.repository.AdherentRepository;
import com.example.repository.OuvrageRepository;
import com.example.repository.PretRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class PretService {

    private static final int DUREE_PRET_JOURS = 21;
    private static final BigDecimal PENALITE_PAR_JOUR = new BigDecimal("0.15");
    private static final int SEUIL_RETARDS_SUSPENSION = 3;

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
        adherent.actualiserPourAnnee(LocalDate.now().getYear());
        adherentRepository.save(adherent);
        if (adherent.isSuspendu()) {
            throw new AdherentSuspenduException(adherentId);
        }
        ouvrageRepository.findByIsbn(ouvrageIsbn)
                .orElseThrow(() -> new IllegalArgumentException("Ouvrage introuvable"));

        if (!pretRepository.findEnCoursByOuvrageIsbn(ouvrageIsbn).isEmpty()) {
            throw new OuvrageIndisponibleException(ouvrageIsbn);
        }

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

    public Pret retournerPret(String pretId, LocalDate dateRetourEffective) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new IllegalArgumentException("Prêt introuvable"));

        pret.setDateRetourEffective(dateRetourEffective);

        long joursRetard = ChronoUnit.DAYS.between(pret.getDateRetourPrevue(), dateRetourEffective);
        if (joursRetard > 0) {
            Adherent adherent = adherentRepository.findById(pret.getAdherentId()).orElseThrow();
            int annee = dateRetourEffective.getYear();
            adherent.incrementerRetardImportant(annee);
            if (adherent.getRetardsImportants() >= SEUIL_RETARDS_SUSPENSION) {
                adherent.setSuspendu(true);
            }
            adherentRepository.save(adherent);
        }

        return pret;
    }

    public BigDecimal calculerPenalite(Pret pret, LocalDate dateRetourEffective) {
        long joursRetard = ChronoUnit.DAYS.between(pret.getDateRetourPrevue(), dateRetourEffective);
        if (joursRetard <= 0) {
            return BigDecimal.ZERO;
        }
        return PENALITE_PAR_JOUR.multiply(BigDecimal.valueOf(joursRetard));
    }

    public boolean estOuvrageDisponible(String ouvrageIsbn) {
        return pretRepository.findEnCoursByOuvrageIsbn(ouvrageIsbn).isEmpty();
    }
}
