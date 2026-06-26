package com.example.exception;

public class AdherentSuspenduException extends RuntimeException {

    public AdherentSuspenduException(String adherentId) {
        super("L'adhérent " + adherentId + " est suspendu");
    }
}
