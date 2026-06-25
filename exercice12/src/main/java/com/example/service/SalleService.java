package com.example.service;

import com.example.model.Salle;
import com.example.repository.SalleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalleService {

    private final SalleRepository salleRepository;

    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    public Salle creer(String nom, int capacite) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (capacite < 1) {
            throw new IllegalArgumentException("La capacité doit être supérieure ou égale à 1");
        }
        return salleRepository.save(nom.trim(), capacite);
    }

    public List<Salle> listerToutes() {
        return salleRepository.findAll();
    }
}
