package com.kelompok13.doodlequest.core;

import com.kelompok13.doodlequest.common.Constants;
import com.kelompok13.doodlequest.state.MenuState;
import com.kelompok13.doodlequest.state.StateManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

public final class Main extends Application {
    private GameLoop loop;
    @Override public void start(Stage stage) {
        Canvas canvas=new Canvas(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
        StackPane root=new StackPane(canvas);
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        Scene scene=new Scene(root,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
        StateManager states=new StateManager();
        states.change(new MenuState(states));
        scene.addEventHandler(KeyEvent.KEY_PRESSED,e->states.onKeyPressed(e.getCode()));
        scene.addEventHandler(
                KeyEvent.KEY_TYPED,
                e -> states.onKeyTyped(e.getCharacter())
        );
        scene.addEventHandler(KeyEvent.KEY_RELEASED,e->states.onKeyReleased(e.getCode()));
        stage.setTitle("Doodle Quest: Beyond the Sky - Desktop Landscape");
        stage.setMinWidth(900);stage.setMinHeight(540);
        stage.setScene(scene);stage.show();
        loop=new GameLoop(canvas,states);loop.start();
    }
    @Override public void stop(){if(loop!=null)loop.stop();}
    public static void main(String[] args){launch(args);}
}
