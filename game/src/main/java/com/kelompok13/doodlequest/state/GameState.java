package com.kelompok13.doodlequest.state;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

public interface GameState {
    void enter();
    void update(double dt);
    void draw(GraphicsContext gc);
    void handleKey(KeyEvent event, boolean pressed);
    void exit();
}