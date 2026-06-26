package com.example.controller;

import com.example.dto.ApiError;
import com.example.dto.CompteResponse;
import com.example.dto.CreateCompteRequest;
import com.example.dto.MontantRequest;
import com.example.dto.TransferRequest;
import com.example.exception.CompteAlreadyExistsException;
import com.example.exception.CompteConflictException;
import com.example.exception.CompteNotFoundException;
import com.example.service.CompteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class CompteController {

    private final CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }

    @PostMapping
    public ResponseEntity<CompteResponse> creer(@Valid @RequestBody CreateCompteRequest request) {
        var compte = compteService.creer(request.getNumero(), request.getTitulaire());
        var response = CompteResponse.from(compte);
        return ResponseEntity.created(URI.create("/accounts/" + response.getNumero())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CompteResponse>> listerTous() {
        var comptes = compteService.listerTous().stream()
                .map(CompteResponse::from)
                .toList();
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/{number}")
    public ResponseEntity<CompteResponse> trouverParNumero(@PathVariable("number") String number) {
        return ResponseEntity.ok(CompteResponse.from(compteService.trouverParNumero(number)));
    }

    @PostMapping("/{number}/deposit")
    public ResponseEntity<CompteResponse> deposer(
            @PathVariable("number") String number,
            @Valid @RequestBody MontantRequest request) {
        return ResponseEntity.ok(CompteResponse.from(compteService.deposer(number, request.getMontant())));
    }

    @PostMapping("/{number}/withdraw")
    public ResponseEntity<CompteResponse> retirer(
            @PathVariable("number") String number,
            @Valid @RequestBody MontantRequest request) {
        return ResponseEntity.ok(CompteResponse.from(compteService.retirer(number, request.getMontant())));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> virer(@Valid @RequestBody TransferRequest request) {
        compteService.virer(request.getCompteSource(), request.getCompteDestination(), request.getMontant());
        return ResponseEntity.ok().build();
    }
}

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(CompteNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(CompteNotFoundException exception) {
        return ResponseEntity.status(404).body(ApiError.of(404, exception.getMessage()));
    }

    @ExceptionHandler(CompteAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(CompteAlreadyExistsException exception) {
        return ResponseEntity.status(409).body(ApiError.of(409, exception.getMessage()));
    }

    @ExceptionHandler(CompteConflictException.class)
    public ResponseEntity<ApiError> handleConflict(CompteConflictException exception) {
        return ResponseEntity.status(409).body(ApiError.of(409, exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(ApiError.of(400, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Requête invalide");
        return ResponseEntity.badRequest().body(ApiError.of(400, message));
    }
}
