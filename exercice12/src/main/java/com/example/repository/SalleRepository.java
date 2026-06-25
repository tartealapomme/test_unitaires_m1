package com.example.repository;

import com.example.model.Salle;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SalleRepository {

    private final Map<Long, Salle> salles = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Salle save(String nom, int capacite) {
        long id = idGenerator.incrementAndGet();
        Salle salle = new Salle(id, nom, capacite);
        salles.put(id, salle);
        return salle;
    }

    public Optional<Salle> findById(Long id) {
        return Optional.ofNullable(salles.get(id));
    }

    public List<Salle> findAll() {
        return new ArrayList<>(salles.values());
    }

    public void deleteAll() {
        salles.clear();
        idGenerator.set(0);
    }
}
