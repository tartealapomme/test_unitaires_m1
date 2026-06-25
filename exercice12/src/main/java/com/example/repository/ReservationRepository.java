package com.example.repository;

import com.example.model.Reservation;
import com.example.model.StatutReservation;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationRepository {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Reservation save(Long salleId, String nomPersonne,
                            LocalDateTime debut, LocalDateTime fin, StatutReservation statut) {
        long id = idGenerator.incrementAndGet();
        Reservation reservation = new Reservation(id, salleId, nomPersonne, debut, fin, statut);
        reservations.put(id, reservation);
        return reservation;
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public List<Reservation> findBySalleId(Long salleId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getSalleId().equals(salleId))
                .toList();
    }

    public void deleteAll() {
        reservations.clear();
        idGenerator.set(0);
    }
}
