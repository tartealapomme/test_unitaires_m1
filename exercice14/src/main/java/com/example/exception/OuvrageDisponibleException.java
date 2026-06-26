package com.example.exception;

public class OuvrageDisponibleException extends RuntimeException {

    public OuvrageDisponibleException(String isbn) {
        super("L'ouvrage " + isbn + " est disponible, la réservation n'est pas nécessaire");
    }
}
