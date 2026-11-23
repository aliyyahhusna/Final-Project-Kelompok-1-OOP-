
package com.kelompok13.doodlequest.enemy;

import com.kelompok13.doodlequest.common.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Projectile extends GameObject {

    private double velocityX;
    private double velocityY;

    private static final double SIZE = 12;

    public Projectile() {
        super(0, 0, SIZE, SIZE);
        active = false;
    }

    public void activate(
            double x,
            double y,
            double velocityX,
            double velocityY) {

        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.active = true;
    }

    public void deactivate() {
        active = false;
        velocityX = 0;
        velocityY = 0;
    }

    @Override
    public void update(double dt) {
        if (!active || dt <= 0) {
            return;
        }

        x += velocityX * dt;
        y += velocityY * dt;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!active) {
            return;
        }

        gc.setFill(Color.RED);
        gc.fillOval(x, y, width, height);
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }
}
