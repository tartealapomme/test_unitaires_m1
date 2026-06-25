package com.example.controller;

import com.example.dto.CreateSalleRequest;
import com.example.dto.SalleResponse;
import com.example.service.SalleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class SalleController {

    private final SalleService salleService;

    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }

    @PostMapping
    public ResponseEntity<SalleResponse> creer(@Valid @RequestBody CreateSalleRequest request) {
        var salle = salleService.creer(request.getNom(), request.getCapacite());
        var response = SalleResponse.from(salle);
        return ResponseEntity.created(URI.create("/api/rooms/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SalleResponse>> lister() {
        var salles = salleService.listerToutes().stream()
                .map(SalleResponse::from)
                .toList();
        return ResponseEntity.ok(salles);
    }
}
