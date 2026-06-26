package com.example.model;

public class Ouvrage {

    private final String isbn;
    private final String titre;

    public Ouvrage(String isbn, String titre) {
        this.isbn = isbn;
        this.titre = titre;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitre() {
        return titre;
    }
}
