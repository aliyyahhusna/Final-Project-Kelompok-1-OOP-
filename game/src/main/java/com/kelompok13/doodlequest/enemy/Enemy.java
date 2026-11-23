
package com.kelompok13.doodlequest.enemy;

import com.kelompok13.doodlequest.common.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Enemy extends GameObject {

    private final EnemyType type;

    protected Enemy(double x, double y, EnemyType type) {
        super(x, y, 64, 64);
        this.type = type;
    }

    public EnemyType getType() {
        return type;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void defeat() {
        active = false;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!active) {
            return;
        }

        if (type == EnemyType.STATIONARY) {
            gc.setFill(Color.CRIMSON);
        } else {
            gc.setFill(Color.ORANGE);
        }

        gc.fillOval(x, y, width, height);
    }
}
