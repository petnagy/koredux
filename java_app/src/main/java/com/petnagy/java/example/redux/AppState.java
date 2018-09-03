package com.petnagy.java.example.redux;

public class AppState {

    private final int counter;

    public AppState(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }
}
