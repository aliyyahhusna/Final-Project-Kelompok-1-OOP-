package com.kelompok13.doodlequest.service;

public interface ScoreService {
    boolean register(String username,String password);
    boolean login(String username,String password);
    void saveScore(int score);
    int getHighScore();
}
