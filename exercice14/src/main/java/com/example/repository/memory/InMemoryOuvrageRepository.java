package com.example.repository.memory;

import com.example.model.Ouvrage;
import com.example.repository.OuvrageRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryOuvrageRepository implements OuvrageRepository {

    private final Map<String, Ouvrage> ouvrages = new HashMap<>();

    @Override
    public Optional<Ouvrage> findByIsbn(String isbn) {
        return Optional.ofNullable(ouvrages.get(isbn));
    }

    public Ouvrage save(Ouvrage ouvrage) {
        ouvrages.put(ouvrage.getIsbn(), ouvrage);
        return ouvrage;
    }

    public void clear() {
        ouvrages.clear();
    }
}
