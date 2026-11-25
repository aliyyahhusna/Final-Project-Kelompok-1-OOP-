package com.kelompok13.doodlequest.state;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public final class StateManager {
    private GameState current;
    public GameState getCurrent() {return current;}
    public void change(GameState next) {
        if(current!=null) current.onExit();
        current=next;
        if(current!=null) current.onEnter();
    }
    public void update(double dt) {if(current!=null) current.update(dt);}
    public void draw(GraphicsContext gc) {if(current!=null) current.draw(gc);}
    public void onKeyPressed(KeyCode k) {if(current!=null) current.onKeyPressed(k);}
    public void onKeyReleased(KeyCode k) {if(current!=null) current.onKeyReleased(k);}
    public void onKeyTyped(String character) {
        if (current != null) {
            current.onKeyTyped(character);
        }
    }
}
