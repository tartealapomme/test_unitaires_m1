package com.example.exception;

public class CompteConflictException extends RuntimeException {

    public CompteConflictException(String message) {
        super(message);
    }
}
