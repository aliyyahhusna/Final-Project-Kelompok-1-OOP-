package com.kelompok13.doodlequest.state;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public abstract class GameState {
    protected final StateManager manager;
    protected GameState(StateManager manager) { this.manager=manager; }
    public void onEnter() {}
    public void onExit() {}
    public abstract void update(double dt);
    public abstract void draw(GraphicsContext gc);
    public void onKeyPressed(KeyCode key) {}
    public void onKeyReleased(KeyCode key) {}
    public void onKeyTyped(String character) {}
}
