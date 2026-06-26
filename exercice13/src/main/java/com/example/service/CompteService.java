package com.example.service;

import com.example.exception.CompteAlreadyExistsException;
import com.example.exception.CompteConflictException;
import com.example.exception.CompteNotFoundException;
import com.example.model.Compte;
import com.example.repository.CompteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompteService {

    private final CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public Compte creer(String numero, String titulaire) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Le numéro de compte est obligatoire");
        }
        if (titulaire == null || titulaire.isBlank()) {
            throw new IllegalArgumentException("Le titulaire est obligatoire");
        }
        if (compteRepository.existsByNumero(numero.trim())) {
            throw new CompteAlreadyExistsException(numero.trim());
        }
        return compteRepository.save(numero.trim(), titulaire.trim());
    }

    public Compte trouverParNumero(String numero) {
        return compteRepository.findByNumero(numero)
                .orElseThrow(() -> new CompteNotFoundException(numero));
    }

    public List<Compte> listerTous() {
        return compteRepository.findAll();
    }

    public Compte deposer(String numero, BigDecimal montant) {
        Compte compte = trouverParNumero(numero);
        validerMontantStrictementPositif(montant);
        compte.setSolde(compte.getSolde().add(montant));
        return compte;
    }

    public Compte retirer(String numero, BigDecimal montant) {
        Compte compte = trouverParNumero(numero);
        validerMontantStrictementPositif(montant);
        if (compte.getSolde().compareTo(montant) < 0) {
            throw new CompteConflictException("Solde insuffisant pour effectuer le retrait");
        }
        compte.setSolde(compte.getSolde().subtract(montant));
        return compte;
    }

    public void virer(String numeroSource, String numeroDestination, BigDecimal montant) {
        validerMontantStrictementPositif(montant);
        Compte source = compteRepository.findByNumero(numeroSource)
                .orElseThrow(() -> new CompteNotFoundException(numeroSource));
        if (compteRepository.findByNumero(numeroDestination).isEmpty()) {
            throw new CompteNotFoundException(numeroDestination);
        }
        if (source.getSolde().compareTo(montant) < 0) {
            throw new CompteConflictException("Solde insuffisant pour effectuer le virement");
        }
        retirer(numeroSource, montant);
        deposer(numeroDestination, montant);
    }

    private void validerMontantStrictementPositif(BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être strictement positif");
        }
    }
}
