package com.kelompok13.doodlequest.core;

import com.kelompok13.doodlequest.common.Constants;
import com.kelompok13.doodlequest.state.StateManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class GameLoop extends AnimationTimer {
    private final Canvas canvas;
    private final StateManager manager;
    private long last=0;
    public GameLoop(Canvas canvas,StateManager manager){this.canvas=canvas;this.manager=manager;}
    @Override public void handle(long now) {
        if(last==0){last=now;return;}
        double dt=Math.min((now-last)/1_000_000_000.0,0.05);last=now;
        manager.update(dt);
        GraphicsContext gc=canvas.getGraphicsContext2D();
        gc.setTransform(1,0,0,1,0,0);
        gc.setFill(Color.rgb(12,22,39));
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        double s=Math.min(canvas.getWidth()/Constants.WIDTH,canvas.getHeight()/Constants.HEIGHT);
        double ox=(canvas.getWidth()-Constants.WIDTH*s)/2;
        double oy=(canvas.getHeight()-Constants.HEIGHT*s)/2;
        gc.save();
        gc.setTransform(s,0,0,s,ox,oy);
        manager.draw(gc);
        gc.restore();
    }
    @Override public void stop(){super.stop();last=0;}
}
