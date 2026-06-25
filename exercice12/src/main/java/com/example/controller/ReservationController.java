package com.example.controller;

import com.example.dto.CreateReservationRequest;
import com.example.dto.ReservationResponse;
import com.example.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> creer(@Valid @RequestBody CreateReservationRequest request) {
        var reservation = reservationService.creer(
                request.getSalleId(),
                request.getNomPersonne(),
                request.getDebut(),
                request.getFin()
        );
        var response = ReservationResponse.from(reservation);
        return ResponseEntity.created(URI.create("/api/reservations/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationResponse.from(reservationService.trouverParId(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponse> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationResponse.from(reservationService.annuler(id)));
    }
}
