package com.kelompok13.doodlequest.ui;

import com.kelompok13.doodlequest.core.GameObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class Hud implements GameObserver {
    private int level=1,lives=3,score=0;
    private double height=0;
    @Override public void onGameUpdated(int lev,int hp,int sc,double maxH) {
        level=lev;lives=hp;score=sc;height=maxH;
    }
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.rgb(13,29,50,0.75));gc.fillRoundRect(20,18,520,66,16,16);
        gc.setFill(Color.WHITE);gc.setFont(Font.font("Poppins",23));
        gc.fillText("LEVEL " + level, 40,58);
        gc.fillText("NYAWA " + lives,180,58);
        gc.fillText("SKOR " + score,350,58);
        gc.setFont(Font.font("Poppins",19));gc.fillText("TINGGI: "+(int)height,1040,55);
    }
}
