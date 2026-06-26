package com.example.repository;

import com.example.model.Compte;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CompteRepository {

    private final Map<String, Compte> comptes = new ConcurrentHashMap<>();

    public Compte save(String numero, String titulaire) {
        Compte compte = new Compte(numero, titulaire, BigDecimal.ZERO);
        comptes.put(numero, compte);
        return compte;
    }

    public Optional<Compte> findByNumero(String numero) {
        return Optional.ofNullable(comptes.get(numero));
    }

    public boolean existsByNumero(String numero) {
        return comptes.containsKey(numero);
    }

    public List<Compte> findAll() {
        return new ArrayList<>(comptes.values());
    }

    public void deleteAll() {
        comptes.clear();
    }
}
