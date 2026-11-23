
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.common.Constants;
import com.kelompok13.doodlequest.player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MovingPlatform extends Platform {

    private static final double SPEED = 100;
    private static final double MOVE_RANGE = 100;

    private final double minX;
    private final double maxX;
    private int direction = 1;

    public MovingPlatform(double x, double y) {
        super(x, y);

        minX = Math.max(0, x - MOVE_RANGE);
        maxX = Math.min(Constants.WIDTH - width, x + MOVE_RANGE);
    }

    @Override
    public void update(double dt) {
        if (!active) {
            return;
        }

        x += direction * SPEED * dt;

        if (x >= maxX) {
            x = maxX;
            direction = -1;
        } else if (x <= minX) {
            x = minX;
            direction = 1;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!active) {
            return;
        }

        gc.setFill(Color.DODGERBLUE);
        gc.fillRoundRect(x, y, width, height, 12, 12);
    }

    @Override
    public void onLanded(Player player) {
        if (active) {
            // TODO: Aktifkan player.bounce()
            // setelah Player dari Zefran siap.
        }
    }
}
