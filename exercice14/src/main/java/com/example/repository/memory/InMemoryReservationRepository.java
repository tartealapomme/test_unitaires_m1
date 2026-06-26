package com.example.repository.memory;

import com.example.model.Reservation;
import com.example.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryReservationRepository implements ReservationRepository {

    private final Map<String, Reservation> reservations = new HashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public List<Reservation> findByOuvrageIsbn(String isbn) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getOuvrageIsbn().equals(isbn))
                .sorted(Comparator.comparing(Reservation::getDateReservation))
                .toList();
    }

    public void remove(String id) {
        reservations.remove(id);
    }

    @Override
    public void deleteById(String id) {
        reservations.remove(id);
    }

    public void clear() {
        reservations.clear();
    }
}
