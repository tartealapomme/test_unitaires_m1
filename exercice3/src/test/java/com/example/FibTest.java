package com.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FibTest {

    @Test
    void getFibSeriesWithRange1() {
        Fib fib = new Fib(1);

        List<Integer> result = fib.getFibSeries();

        assertFalse(result.isEmpty());
        assertEquals(List.of(0), result);
    }

    @Test
    void getFibSeriesWithRange6() {
        Fib fib = new Fib(6);

        List<Integer> result = fib.getFibSeries();

        assertTrue(result.contains(3));
        assertEquals(6, result.size());
        assertFalse(result.contains(4));
        assertEquals(List.of(0, 1, 1, 2, 3, 5), result);
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i) <= result.get(i + 1));
        }
    }
}
