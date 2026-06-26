package com.example.service;

import com.example.exception.CompteAlreadyExistsException;
import com.example.exception.CompteConflictException;
import com.example.exception.CompteNotFoundException;
import com.example.model.Compte;
import com.example.repository.CompteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompteServiceTest {

    @Mock
    private CompteRepository compteRepository;

    @InjectMocks
    private CompteService compteService;

    @Test
    void shouldCreateAccount_whenDataIsValid() {
        when(compteRepository.existsByNumero("FR001")).thenReturn(false);
        when(compteRepository.save("FR001", "Alice"))
                .thenReturn(new Compte("FR001", "Alice", BigDecimal.ZERO));

        Compte result = compteService.creer("FR001", "Alice");

        assertEquals("FR001", result.getNumero());
        assertEquals("Alice", result.getTitulaire());
        assertEquals(BigDecimal.ZERO, result.getSolde());
        verify(compteRepository).save("FR001", "Alice");
    }

    @Test
    void shouldRejectCreation_whenAccountNumberAlreadyExists() {
        when(compteRepository.existsByNumero("FR001")).thenReturn(true);

        assertThrows(CompteAlreadyExistsException.class, () -> compteService.creer("FR001", "Alice"));

        verify(compteRepository, never()).save(any(), any());
    }

    @Test
    void shouldReturnAccount_whenAccountExists() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.ZERO);
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        Compte result = compteService.trouverParNumero("FR001");

        assertEquals("FR001", result.getNumero());
        verify(compteRepository).findByNumero("FR001");
    }

    @Test
    void shouldThrowNotFound_whenAccountDoesNotExist() {
        when(compteRepository.findByNumero("FR999")).thenReturn(Optional.empty());

        assertThrows(CompteNotFoundException.class, () -> compteService.trouverParNumero("FR999"));
    }

    @Test
    void shouldReturnAllAccounts() {
        List<Compte> comptes = List.of(
                new Compte("FR001", "Alice", BigDecimal.TEN),
                new Compte("FR002", "Bob", BigDecimal.ONE)
        );
        when(compteRepository.findAll()).thenReturn(comptes);

        List<Compte> result = compteService.listerTous();

        assertEquals(2, result.size());
        verify(compteRepository).findAll();
    }

    @Test
    void shouldDeposit_whenAmountIsValid() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.valueOf(100));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        Compte result = compteService.deposer("FR001", BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), result.getSolde());
    }

    @Test
    void shouldRejectDeposit_whenAmountIsNull() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.valueOf(100));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        assertThrows(IllegalArgumentException.class, () -> compteService.deposer("FR001", null));
    }

    @Test
    void shouldRejectDeposit_whenAmountIsZero() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.valueOf(100));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        assertThrows(IllegalArgumentException.class, () -> compteService.deposer("FR001", BigDecimal.ZERO));
    }

    @Test
    void shouldRejectDeposit_whenAmountIsNegative() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.valueOf(100));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        assertThrows(IllegalArgumentException.class, () -> compteService.deposer("FR001", BigDecimal.valueOf(-10)));
    }

    @Test
    void shouldWithdraw_whenAmountIsValid() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.valueOf(100));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        Compte result = compteService.retirer("FR001", BigDecimal.valueOf(40));

        assertEquals(BigDecimal.valueOf(60), result.getSolde());
    }

    @Test
    void shouldRejectWithdraw_whenAmountIsNull() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.valueOf(100));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        assertThrows(IllegalArgumentException.class, () -> compteService.retirer("FR001", null));
    }

    @Test
    void shouldRejectWithdraw_whenAmountIsZero() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.valueOf(100));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        assertThrows(IllegalArgumentException.class, () -> compteService.retirer("FR001", BigDecimal.ZERO));
    }

    @Test
    void shouldRejectWithdraw_whenAmountIsNegative() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.valueOf(100));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        assertThrows(IllegalArgumentException.class, () -> compteService.retirer("FR001", BigDecimal.valueOf(-5)));
    }

    @Test
    void shouldRejectWithdraw_whenInsufficientFunds() {
        Compte compte = new Compte("FR001", "Alice", BigDecimal.valueOf(30));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(compte));

        assertThrows(CompteConflictException.class, () -> compteService.retirer("FR001", BigDecimal.valueOf(50)));
    }

    @Test
    void shouldTransfer_whenDataIsValid() {
        Compte source = new Compte("FR001", "Alice", BigDecimal.valueOf(200));
        Compte destination = new Compte("FR002", "Bob", BigDecimal.valueOf(50));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(source));
        when(compteRepository.findByNumero("FR002")).thenReturn(Optional.of(destination));

        compteService.virer("FR001", "FR002", BigDecimal.valueOf(75));

        assertEquals(BigDecimal.valueOf(125), source.getSolde());
        assertEquals(BigDecimal.valueOf(125), destination.getSolde());
    }

    @Test
    void shouldRejectTransfer_whenAmountIsNull() {
        assertThrows(IllegalArgumentException.class, () -> compteService.virer("FR001", "FR002", null));
    }

    @Test
    void shouldRejectTransfer_whenAmountIsZero() {
        assertThrows(IllegalArgumentException.class, () -> compteService.virer("FR001", "FR002", BigDecimal.ZERO));
    }

    @Test
    void shouldRejectTransfer_whenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> compteService.virer("FR001", "FR002", BigDecimal.valueOf(-1)));
    }

    @Test
    void shouldRejectTransfer_whenInsufficientFunds() {
        Compte source = new Compte("FR001", "Alice", BigDecimal.valueOf(20));
        Compte destination = new Compte("FR002", "Bob", BigDecimal.ZERO);
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(source));
        when(compteRepository.findByNumero("FR002")).thenReturn(Optional.of(destination));

        assertThrows(CompteConflictException.class,
                () -> compteService.virer("FR001", "FR002", BigDecimal.valueOf(50)));
    }

    @Test
    void shouldRejectTransfer_whenDestinationAccountDoesNotExist() {
        Compte source = new Compte("FR001", "Alice", BigDecimal.valueOf(100));
        when(compteRepository.findByNumero("FR001")).thenReturn(Optional.of(source));
        when(compteRepository.findByNumero("FR999")).thenReturn(Optional.empty());

        assertThrows(CompteNotFoundException.class,
                () -> compteService.virer("FR001", "FR999", BigDecimal.TEN));
    }

    @Test
    void shouldRejectTransfer_whenSourceAccountDoesNotExist() {
        when(compteRepository.findByNumero("FR999")).thenReturn(Optional.empty());

        assertThrows(CompteNotFoundException.class,
                () -> compteService.virer("FR999", "FR002", BigDecimal.TEN));

        verify(compteRepository, never()).findByNumero("FR002");
    }
}
