package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordValidatorTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @Test
    void shouldAcceptPassword1() {
        assertTrue(validator.isValid("Password1!"));
        assertEquals("Password is valid", validator.getErrorMessage("Password1!"));
    }

    @Test
    void shouldRejectNullPassword() {
        assertFalse(validator.isValid(null));
        assertEquals("Password must not be null", validator.getErrorMessage(null));
    }

    @Test
    void shouldRejectPasswordWithoutLowercase() {
        assertFalse(validator.isValid("PASSWORD1!"));
        assertEquals("Password must contain at least one lowercase letter", validator.getErrorMessage("PASSWORD1!"));
    }

    @Test
    void shouldRejectPasswordWithoutSpecialCharacter() {
        assertFalse(validator.isValid("Password1"));
        assertEquals("Password must contain at least one special character", validator.getErrorMessage("Password1"));
    }

    @ParameterizedTest
    @CsvSource({
            "Password1!, true",
            "Admin2024@, true",
            "short1!, false",
            "PASSWORD1!, false",
            "password1!, false",
            "Password!, false",
            "Password1, false"
    })
    void shouldValidatePasswordWithCsvSource(String password, boolean expected) {
        assertEquals(expected, validator.isValid(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password1!", "Admin2024@"})
    void shouldAcceptValidPasswordsWithValueSource(String password) {
        assertTrue(validator.isValid(password));
        assertEquals("Password is valid", validator.getErrorMessage(password));
    }

    static Stream<Arguments> passwordMessageCases() {
        return Stream.of(
                Arguments.of(null, "Password must not be null"),
                Arguments.of("short1!", "Password must contain at least 8 characters"),
                Arguments.of("PASSWORD1!", "Password must contain at least one lowercase letter"),
                Arguments.of("password1!", "Password must contain at least one uppercase letter"),
                Arguments.of("Password!", "Password must contain at least one digit"),
                Arguments.of("Password1", "Password must contain at least one special character"),
                Arguments.of("Password1!", "Password is valid")
        );
    }

    @ParameterizedTest
    @MethodSource("passwordMessageCases")
    void shouldReturnExpectedMessageWithMethodSource(String password, String expectedMessage) {
        assertEquals(expectedMessage, validator.getErrorMessage(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectNullAndEmptyWithNullAndEmptySource(String password) {
        assertFalse(validator.isValid(password));
    }
}
