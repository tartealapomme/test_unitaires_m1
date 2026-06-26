package com.example.exception;

public class CompteNotFoundException extends RuntimeException {

    public CompteNotFoundException(String numero) {
        super("Aucun compte trouvé avec le numéro " + numero);
    }
}
