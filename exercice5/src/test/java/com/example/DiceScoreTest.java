package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiceScoreTest {

    @Mock
    private Ide de;

    private DiceScore diceScore;

    @BeforeEach
    void setUp() {
        diceScore = new DiceScore(de);
    }

    @Test
    void shouldReturnDoublePlusTenWhenRollsAreEqual() {
        when(de.getRoll()).thenReturn(3, 3);

        assertEquals(16, diceScore.getScore());
    }

    @Test
    void shouldReturnThirtyWhenRollsAreEqualToSix() {
        when(de.getRoll()).thenReturn(6, 6);

        assertEquals(30, diceScore.getScore());
    }

    @Test
    void shouldReturnHighestRollWhenSecondRollIsHigher() {
        when(de.getRoll()).thenReturn(2, 5);

        assertEquals(5, diceScore.getScore());
    }

    @Test
    void shouldReturnHighestRollWhenFirstRollIsHigher() {
        when(de.getRoll()).thenReturn(5, 2);

        assertEquals(5, diceScore.getScore());
    }

    @ParameterizedTest
    @CsvSource({
            "3, 3, 16",
            "4, 4, 18",
            "6, 6, 30",
            "2, 5, 5",
            "5, 2, 5",
            "1, 6, 6"
    })
    void shouldComputeExpectedScore(int firstRoll, int secondRoll, int expectedScore) {
        when(de.getRoll()).thenReturn(firstRoll, secondRoll);

        assertEquals(expectedScore, diceScore.getScore());
    }

    @Test
    void shouldCallDeTwice() {
        when(de.getRoll()).thenReturn(1, 2);

        diceScore.getScore();

        verify(de, times(2)).getRoll();
    }
}
