package com.kelompok13.doodlequest.core;

import com.kelompok13.doodlequest.common.Constants;
import java.util.ArrayList;
import java.util.List;

public final class GameManager {
    private static final GameManager INSTANCE = new GameManager();
    public static GameManager getInstance() { return INSTANCE; }
    private final List<GameObserver> observers = new ArrayList<>();
    private int level=1, lives=3, score=0;
    private double maxHeight=0;
    private GameManager() { }
    public void addObserver(GameObserver o) { if(o!=null && !observers.contains(o)) observers.add(o); }
    public void removeObserver(GameObserver o) { observers.remove(o); }
    private void notifyObservers() {
        for(GameObserver o : List.copyOf(observers)) o.onGameUpdated(level,lives,score,maxHeight);
    }
    public void reset() { level=1;lives=3;score=0;maxHeight=0;notifyObservers(); }
    public int getLevel() { return level; }
    public int getLives() { return lives; }
    public int getScore() { return score; }
    public double getMaxHeight() { return maxHeight; }
    public void setLevel(int value) { level=Math.max(1,Math.min(3,value)); notifyObservers(); }
    public void setLives(int value) { lives=Math.max(0,value);notifyObservers(); }
    public void loseLife() { setLives(lives-1); }
    public void addScore(int points) { score=Math.max(0,score+points);notifyObservers(); }
    public void setMaxHeight(double value) { if(value>maxHeight){maxHeight=value;notifyObservers();} }
}
