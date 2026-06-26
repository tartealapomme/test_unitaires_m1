package com.example.repository;

import com.example.model.Ouvrage;

import java.util.Optional;

public interface OuvrageRepository {

    Optional<Ouvrage> findByIsbn(String isbn);
}
