package com.example.repository;

import com.example.model.Reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findByOuvrageIsbn(String isbn);

    void deleteById(String id);
}
