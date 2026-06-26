package com.example.exception;

public class CompteAlreadyExistsException extends RuntimeException {

    public CompteAlreadyExistsException(String numero) {
        super("Un compte existe déjà avec le numéro " + numero);
    }
}
