package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RechercheVilleTest {

    private static final List<String> TOUTES_LES_VILLES = List.of(
            "Paris", "Budapest", "Skopje", "Rotterdam", "Valence", "Vancouver",
            "Amsterdam", "Vienne", "Sydney", "New York", "Londres", "Bangkok",
            "Hong Kong", "Dubaï", "Rome", "Istanbul"
    );

    @Mock
    private SourceVilles sourceVilles;

    private RechercheVille rechercheVille;

    @BeforeEach
    void setUp() {
        when(sourceVilles.getVilles()).thenReturn(TOUTES_LES_VILLES);
        rechercheVille = new RechercheVille(sourceVilles);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "a"})
    void shouldThrowNotFoundExceptionWhenSearchTextHasLessThanTwoCharacters(String mot) {
        assertThrows(NotFoundException.class, () -> rechercheVille.rechercher(mot));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSearchTextHasOneCharacter() {
        assertThrows(NotFoundException.class, () -> rechercheVille.rechercher("P"));
    }

    @Test
    void shouldReturnCitiesStartingWithSearchText() {
        List<String> result = rechercheVille.rechercher("Va");

        assertEquals(List.of("Valence", "Vancouver"), result);
    }

    @Test
    void shouldBeCaseInsensitive() {
        List<String> result = rechercheVille.rechercher("va");

        assertEquals(List.of("Valence", "Vancouver"), result);
    }

    @Test
    void shouldReturnCityWhenSearchTextIsPartOfName() {
        List<String> result = rechercheVille.rechercher("ape");

        assertEquals(List.of("Budapest"), result);
    }

    @Test
    void shouldReturnAllCitiesWhenSearchTextIsAsterisk() {
        List<String> result = rechercheVille.rechercher("*");

        assertEquals(TOUTES_LES_VILLES, result);
    }

    @ParameterizedTest
    @CsvSource({
            "Par, Paris",
            "Rot, Rotterdam",
            "rom, Rome"
    })
    void shouldReturnMatchingCityWithCsvSource(String mot, String villeAttendue) {
        List<String> result = rechercheVille.rechercher(mot);

        assertEquals(List.of(villeAttendue), result);
    }

    @Test
    void shouldLoadCitiesFromSource() {
        rechercheVille.rechercher("Par");

        verify(sourceVilles).getVilles();
    }
}
