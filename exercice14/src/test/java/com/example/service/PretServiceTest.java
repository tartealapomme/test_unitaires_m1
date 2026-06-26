package com.example.service;

import com.example.exception.AdherentSuspenduException;
import com.example.exception.OuvrageIndisponibleException;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
    void shouldCreateLoanWithReturnDateIn21Days_whenAdherentAndBookExist() {
        when(adherentRepository.findById("A1"))
                .thenReturn(Optional.of(new Adherent("A1", "Alice")));
        when(ouvrageRepository.findByIsbn("978-1"))
                .thenReturn(Optional.of(new Ouvrage("978-1", "Le Petit Prince")));
        when(pretRepository.findEnCoursByOuvrageIsbn("978-1")).thenReturn(List.of());
        when(pretRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Pret pret = pretService.creerPret("A1", "978-1");

        assertThat(pret.getAdherentId()).isEqualTo("A1");
        assertThat(pret.getOuvrageIsbn()).isEqualTo("978-1");
        assertThat(pret.getDateRetourPrevue()).isEqualTo(LocalDate.now().plusDays(21));
    }

    @Test
    void shouldRejectLoan_whenBookIsAlreadyBorrowed() {
        Pret pretEnCours = new Pret("P1", "A2", "978-1",
                LocalDate.now(), LocalDate.now().plusDays(21));
        when(adherentRepository.findById("A1"))
                .thenReturn(Optional.of(new Adherent("A1", "Alice")));
        when(ouvrageRepository.findByIsbn("978-1"))
                .thenReturn(Optional.of(new Ouvrage("978-1", "Le Petit Prince")));
        when(pretRepository.findEnCoursByOuvrageIsbn("978-1")).thenReturn(List.of(pretEnCours));

        assertThatThrownBy(() -> pretService.creerPret("A1", "978-1"))
                .isInstanceOf(OuvrageIndisponibleException.class);

        verify(pretRepository, never()).save(any());
    }

    @Test
    void shouldRejectLoan_whenAdherentIsSuspended() {
        Adherent adherent = new Adherent("A1", "Alice");
        adherent.setSuspendu(true);
        when(adherentRepository.findById("A1")).thenReturn(Optional.of(adherent));

        assertThatThrownBy(() -> pretService.creerPret("A1", "978-1"))
                .isInstanceOf(AdherentSuspenduException.class);

        verify(pretRepository, never()).save(any());
    }

    @Test
    void shouldCalculatePenaltyAt015PerDay_whenReturnIsLate() {
        Pret pret = new Pret("P1", "A1", "978-1",
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 22));

        BigDecimal penalite = pretService.calculerPenalite(pret, LocalDate.of(2025, 1, 25));

        assertThat(penalite).isEqualByComparingTo(new BigDecimal("0.45"));
    }

    @Test
    void shouldReturnZeroPenalty_whenReturnIsOnTime() {
        Pret pret = new Pret("P1", "A1", "978-1",
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 22));

        BigDecimal penalite = pretService.calculerPenalite(pret, LocalDate.of(2025, 1, 22));

        assertThat(penalite).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldNotSuspendAdherent_beforeThreeImportantLateReturnsInSameYear() {
        Adherent adherent = new Adherent("A1", "Alice");
        when(adherentRepository.findById("A1")).thenReturn(Optional.of(adherent));
        when(adherentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate dateRetour = LocalDate.of(2025, 6, 1);
        Pret pret1 = new Pret("P1", "A1", "978-1", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 22));
        Pret pret2 = new Pret("P2", "A1", "978-2", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 22));

        when(pretRepository.findById("P1")).thenReturn(Optional.of(pret1));
        when(pretRepository.findById("P2")).thenReturn(Optional.of(pret2));

        pretService.retournerPret("P1", dateRetour);
        pretService.retournerPret("P2", dateRetour);

        assertThat(adherent.getRetardsImportants()).isEqualTo(2);
        assertThat(adherent.isSuspendu()).isFalse();
    }

    @Test
    void shouldSuspendAdherent_afterThreeImportantLateReturnsInSameYear() {
        Adherent adherent = new Adherent("A1", "Alice");
        when(adherentRepository.findById("A1")).thenReturn(Optional.of(adherent));
        when(adherentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate dateRetour = LocalDate.of(2025, 6, 1);
        Pret pret1 = new Pret("P1", "A1", "978-1", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 22));
        Pret pret2 = new Pret("P2", "A1", "978-2", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 22));
        Pret pret3 = new Pret("P3", "A1", "978-3", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 22));

        when(pretRepository.findById("P1")).thenReturn(Optional.of(pret1));
        when(pretRepository.findById("P2")).thenReturn(Optional.of(pret2));
        when(pretRepository.findById("P3")).thenReturn(Optional.of(pret3));

        pretService.retournerPret("P1", dateRetour);
        pretService.retournerPret("P2", dateRetour);
        pretService.retournerPret("P3", dateRetour);

        assertThat(adherent.isSuspendu()).isTrue();
    }

    @Test
    void shouldResetSuspension_whenNewYearStarts() {
        Adherent adherent = new Adherent("A1", "Alice");
        adherent.incrementerRetardImportant(2024);
        adherent.incrementerRetardImportant(2024);
        adherent.incrementerRetardImportant(2024);
        adherent.setSuspendu(true);
        when(adherentRepository.findById("A1")).thenReturn(Optional.of(adherent));
        when(adherentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(ouvrageRepository.findByIsbn("978-1"))
                .thenReturn(Optional.of(new Ouvrage("978-1", "1984")));
        when(pretRepository.findEnCoursByOuvrageIsbn("978-1")).thenReturn(List.of());
        when(pretRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Pret pret = pretService.creerPret("A1", "978-1");

        assertThat(adherent.isSuspendu()).isFalse();
        assertThat(adherent.getRetardsImportants()).isZero();
        assertThat(pret.getAdherentId()).isEqualTo("A1");
    }
}
