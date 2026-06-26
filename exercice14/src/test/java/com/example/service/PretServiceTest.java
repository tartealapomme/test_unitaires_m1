package com.example.service;

import com.example.model.Adherent;
import com.example.model.Ouvrage;
import com.example.model.Pret;
import com.example.repository.AdherentRepository;
import com.example.repository.OuvrageRepository;
import com.example.repository.PretRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PretServiceTest {

    @Mock
    private PretRepository pretRepository;

    @Mock
    private AdherentRepository adherentRepository;

    @Mock
    private OuvrageRepository ouvrageRepository;

    @InjectMocks
    private PretService pretService;

    @Test
    void shouldCreateLoan_whenAdherentAndBookExist() {
        when(adherentRepository.findById("A1"))
                .thenReturn(Optional.of(new Adherent("A1", "Alice")));
        when(ouvrageRepository.findByIsbn("978-1"))
                .thenReturn(Optional.of(new Ouvrage("978-1", "Le Petit Prince")));
        when(pretRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Pret pret = pretService.creerPret("A1", "978-1");

        assertNotNull(pret);
        assertEquals("A1", pret.getAdherentId());
        assertEquals("978-1", pret.getOuvrageIsbn());
        assertEquals(LocalDate.now().plusDays(21), pret.getDateRetourPrevue());
        verify(pretRepository).save(any());
    }
}
