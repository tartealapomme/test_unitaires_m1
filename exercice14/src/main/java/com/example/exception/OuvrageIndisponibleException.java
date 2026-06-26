package com.example.exception;

public class OuvrageIndisponibleException extends RuntimeException {

    public OuvrageIndisponibleException(String isbn) {
        super("L'ouvrage " + isbn + " est déjà emprunté");
    }
}
