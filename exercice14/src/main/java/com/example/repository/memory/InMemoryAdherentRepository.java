package com.example.repository.memory;

import com.example.model.Adherent;
import com.example.repository.AdherentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAdherentRepository implements AdherentRepository {

    private final Map<String, Adherent> adherents = new HashMap<>();

    @Override
    public Optional<Adherent> findById(String id) {
        return Optional.ofNullable(adherents.get(id));
    }

    @Override
    public Adherent save(Adherent adherent) {
        adherents.put(adherent.getId(), adherent);
        return adherent;
    }

    public void clear() {
        adherents.clear();
    }
}
