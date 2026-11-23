package com.kelompok13.doodlequest.service;

public final class MockScoreService implements ScoreService {
    private int best;
    public boolean register(String u,String p){return !u.isBlank()&&!p.isBlank();}
    public boolean login(String u,String p){return !u.isBlank()&&!p.isBlank();}
    public void saveScore(int score){best=Math.max(best,score);}
    public int getHighScore(){return best;}
}
