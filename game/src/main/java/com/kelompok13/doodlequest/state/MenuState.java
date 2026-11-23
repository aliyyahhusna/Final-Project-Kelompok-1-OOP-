package com.kelompok13.doodlequest.state;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class MenuState extends GameState {
    public MenuState(StateManager manager){super(manager);}
    @Override public void update(double dt){}
    @Override public void draw(GraphicsContext gc){
        gc.setFill(Color.web("#87c9ef"));gc.fillRect(0,0,1280,720);
        gc.setFill(Color.web("#142f55"));gc.setFont(Font.font("Poppins",62));
        gc.fillText("DOODLE QUEST",390,245);
        gc.setFont(Font.font("Poppins",32));gc.fillText("BEYOND THE SKY",485,295);
    }
    @Override public void onKeyPressed(KeyCode key){
        if(key==KeyCode.ENTER)manager.change(new PlayingState(manager,true));
        if(key==KeyCode.L)manager.change(new LoginState(manager));
    }
}
