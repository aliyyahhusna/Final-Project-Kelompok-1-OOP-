package com.kelompok13.doodlequest.state;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class PausedState extends GameState {
    private final PlayingState previous;
    public PausedState(StateManager m,PlayingState playing){super(m);this.previous=playing;}
    @Override public void update(double dt){}
    @Override public void draw(GraphicsContext gc){
        previous.draw(gc);
        gc.setFill(Color.rgb(0,0,0,0.58));gc.fillRect(0,0,1280,720);
        gc.setFill(Color.WHITE);gc.setFont(Font.font("Poppins",70));gc.fillText("PAUSED",492,330);
        gc.setFont(Font.font("Poppins",26));gc.fillText("P / ESC = Resume  |  M = Menu",432,395);
    }
    @Override public void onKeyPressed(KeyCode k){
        if(k==KeyCode.P||k==KeyCode.ESCAPE)manager.change(previous);
        if(k==KeyCode.M)manager.change(new MenuState(manager));
    }
}
