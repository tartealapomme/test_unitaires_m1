package com.example.repository;

import com.example.model.Pret;

import java.util.List;
import java.util.Optional;

public interface PretRepository {

    Pret save(Pret pret);

    Optional<Pret> findById(String id);

    List<Pret> findEnCoursByOuvrageIsbn(String isbn);

    List<Pret> findByAdherentId(String adherentId);
}
