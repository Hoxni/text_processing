package com.example.ds1.error;

public class WordNotFoundException extends RuntimeException {
    public WordNotFoundException(String word) {
        super("Could not found word: " + word);
    }
}
