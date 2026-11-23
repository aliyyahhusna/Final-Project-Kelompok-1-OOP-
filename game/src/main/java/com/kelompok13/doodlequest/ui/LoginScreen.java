package com.kelompok13.doodlequest.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public final class LoginScreen {
    public void draw(GraphicsContext gc){
        gc.setFill(Color.web("#1f375a"));gc.fillRect(0,0,1280,720);
        gc.setFill(Color.WHITE);gc.setFont(Font.font("Poppins",55));
        gc.fillText("LOGIN",390,260);
        gc.setFont(Font.font("Poppins",26));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("LOGIN", 640, 360);
    }
}
