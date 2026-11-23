package com.kelompok13.doodlequest.state;

import com.kelompok13.doodlequest.core.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class VictoryState extends GameState {
    public VictoryState(StateManager manager){super(manager);}
    @Override public void update(double dt){}
    @Override public void draw(GraphicsContext gc){
        gc.setFill(Color.web("#3b5285"));gc.fillRect(0,0,1280,720);
        gc.setFill(Color.WHITE);gc.setFont(Font.font("Arial",70));gc.fillText("VICTORY!",458,300);
        gc.setFont(Font.font("Poppins",28));gc.fillText("Sky Guardian defeated!",480,365);
        gc.fillText("SKOR: "+GameManager.getInstance().getScore(),533,415);
        gc.fillText("R = Retry  |  M = Menu",458,477);
    }
    @Override public void onKeyPressed(KeyCode k){
        if(k==KeyCode.R)manager.change(new PlayingState(manager,true));
        if(k==KeyCode.M)manager.change(new MenuState(manager));
    }
}
