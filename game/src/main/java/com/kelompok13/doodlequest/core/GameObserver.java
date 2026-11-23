package com.kelompok13.doodlequest.core;

public interface GameObserver {
    void onGameUpdated(int level, int lives, int score, double height);
}
