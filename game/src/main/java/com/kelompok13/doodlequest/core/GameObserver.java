package com.kelompok13.doodlequest.core;

public interface GameObserver {
    void onLivesChanged(int lives);
    void onScoreChanged(int score);
    void onLevelChanged(int level);
}