package com.example.repository.memory;

import com.example.model.Pret;
import com.example.repository.PretRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryPretRepository implements PretRepository {

    private final Map<String, Pret> prets = new HashMap<>();

    @Override
    public Pret save(Pret pret) {
        prets.put(pret.getId(), pret);
        return pret;
    }

    @Override
    public Optional<Pret> findById(String id) {
        return Optional.ofNullable(prets.get(id));
    }

    @Override
    public List<Pret> findEnCoursByOuvrageIsbn(String isbn) {
        return prets.values().stream()
                .filter(pret -> pret.getOuvrageIsbn().equals(isbn) && pret.estEnCours())
                .toList();
    }

    @Override
    public List<Pret> findByAdherentId(String adherentId) {
        return prets.values().stream()
                .filter(pret -> pret.getAdherentId().equals(adherentId))
                .toList();
    }

    public void clear() {
        prets.clear();
    }
}
