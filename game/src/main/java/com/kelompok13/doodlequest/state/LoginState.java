package com.kelompok13.doodlequest.state;

import com.kelompok13.doodlequest.ui.LoginScreen;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;


public final class LoginState extends GameState {
    private final LoginScreen view=new LoginScreen();
    public LoginState(StateManager manager){super(manager);}
    @Override public void update(double dt){}
    @Override public void draw(GraphicsContext gc){view.draw(gc);}
    @Override public void onKeyPressed(KeyCode k){if(k==KeyCode.ENTER||k==KeyCode.ESCAPE)manager.change(new MenuState(manager));}
}
