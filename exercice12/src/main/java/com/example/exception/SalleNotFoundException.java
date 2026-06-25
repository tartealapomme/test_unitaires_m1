package com.example.exception;

public class SalleNotFoundException extends RuntimeException {

    public SalleNotFoundException(Long id) {
        super("Aucune salle trouvée avec l'identifiant " + id);
    }
}
