package com.example.repository;

import com.example.model.Priorite;
import com.example.model.StatutTicket;
import com.example.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TicketRepository {

    private final Map<Long, Ticket> tickets = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Ticket save(String titre, Priorite priorite, StatutTicket statut) {
        long id = idGenerator.incrementAndGet();
        Ticket ticket = new Ticket(id, titre, priorite, statut);
        tickets.put(id, ticket);
        return ticket;
    }

    public Optional<Ticket> findById(Long id) {
        return Optional.ofNullable(tickets.get(id));
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(tickets.values());
    }

    public void deleteAll() {
        tickets.clear();
        idGenerator.set(0);
    }
}
