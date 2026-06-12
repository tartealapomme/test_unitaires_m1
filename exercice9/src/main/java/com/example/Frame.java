package com.example;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private int score;
    private final boolean lastFrame;
    private final IGenerateur generateur;
    private final List<Roll> rolls;

    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
        this.rolls = new ArrayList<>();
    }

    public boolean makeRoll() {
        if (!canRoll()) {
            return false;
        }
        int pins = generateur.randomPin(getMaxPinsForNextRoll());
        rolls.add(new Roll(pins));
        score += pins;
        return true;
    }

    public int getScore() {
        return score;
    }

    private boolean canRoll() {
        if (rolls.isEmpty()) {
            return true;
        }
        if (!lastFrame) {
            if (rolls.get(0).getPins() == 10) {
                return false;
            }
            return rolls.size() < 2;
        }
        if (rolls.size() >= 3) {
            return false;
        }
        if (rolls.size() == 1) {
            return true;
        }
        int first = rolls.get(0).getPins();
        int second = rolls.get(1).getPins();
        return first == 10 || first + second == 10;
    }

    private int getMaxPinsForNextRoll() {
        if (rolls.isEmpty()) {
            return 10;
        }
        if (!lastFrame) {
            return 10 - rolls.get(0).getPins();
        }
        if (rolls.size() == 1) {
            return 10;
        }
        int first = rolls.get(0).getPins();
        int second = rolls.get(1).getPins();
        if (first == 10 && second == 10) {
            return 10;
        }
        if (first == 10) {
            return 10 - second;
        }
        return 10;
    }
}
