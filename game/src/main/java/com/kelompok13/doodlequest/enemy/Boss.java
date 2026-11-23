
package com.kelompok13.doodlequest.enemy;

import com.kelompok13.doodlequest.common.GameObject;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Boss extends GameObject {

    private static final double BOSS_WIDTH = 225;
    private static final double BOSS_HEIGHT = 175;

    // Hitbox kepala lebih kecil daripada tubuh Boss
    private static final double HEAD_WIDTH = 75;
    private static final double HEAD_HEIGHT = 40;

    public Boss(double x, double y) {
        super(x, y, BOSS_WIDTH, BOSS_HEIGHT);
    }

    public Rectangle2D getHeadBounds() {
        double headX = x + (width - HEAD_WIDTH) / 2;
        double headY = y + 10;

        return new Rectangle2D(
                headX,
                headY,
                HEAD_WIDTH,
                HEAD_HEIGHT
        );
    }

    public void defeat() {
        active = false;
    }

    @Override
    public void update(double dt) {
        if (!active) {
            return;
        }



    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!active) {
            return;
        }

        // Tampilan sementara untuk pengujian
        gc.setFill(Color.DARKVIOLET);
        gc.fillRoundRect(x, y, width, height, 25, 25);

        // Menandai area kepala Boss
        Rectangle2D head = getHeadBounds();

        gc.setFill(Color.MEDIUMPURPLE);
        gc.fillOval(
                head.getMinX(),
                head.getMinY(),
                head.getWidth(),
                head.getHeight()
        );
    }
}
