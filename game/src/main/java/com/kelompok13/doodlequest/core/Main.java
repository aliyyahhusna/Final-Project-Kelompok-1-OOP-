package com.kelompok13.doodlequest.core;

import com.kelompok13.doodlequest.common.Constants;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Doodle Quest: Beyond the Sky");
        stage.setScene(new Scene(new StackPane(), Constants.WIDTH, Constants.HEIGHT));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}