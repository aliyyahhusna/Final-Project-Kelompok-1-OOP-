package com.kelompok13.doodlequest.state;

import com.kelompok13.doodlequest.core.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class GameOverState extends GameState {
    public GameOverState(StateManager manager){super(manager);}
    @Override public void update(double dt){}
    @Override public void draw(GraphicsContext gc){
        gc.setFill(Color.web("#17243d"));gc.fillRect(0,0,1280,720);
        gc.setFill(Color.WHITE);gc.setFont(Font.font("Arial",78));gc.fillText("GAME OVER",405,310);
        gc.setFont(Font.font("Arial",30));gc.fillText("SKOR: "+GameManager.getInstance().getScore(),525,380);
        gc.fillText("R = ULANG  |  M = MENU",449,455);
    }
    @Override public void onKeyPressed(KeyCode k){
        if(k==KeyCode.R)manager.change(new PlayingState(manager,true));
        if(k==KeyCode.M)manager.change(new MenuState(manager));
    }
}
