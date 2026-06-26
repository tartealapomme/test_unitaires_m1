package com.example.repository;

import com.example.model.Adherent;

import java.util.Optional;

public interface AdherentRepository {

    Optional<Adherent> findById(String id);
}
