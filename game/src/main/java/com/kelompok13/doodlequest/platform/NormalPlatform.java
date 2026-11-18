
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class NormalPlatform extends Platform {

    public NormalPlatform(double x, double y) {
        super(x, y);
    }

    @Override
    public void update(double dt) {
        // Normal Platform tidak bergerak.
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!active) {
            return;
        }

        gc.setFill(Color.FORESTGREEN);
        gc.fillRoundRect(x, y, width, height, 12, 12);
    }


    @Override
    public void onLanded(Player player) {
        if (active) {
            // TODO: Panggil player.bounce()
            // setelah Player dari Jepran siap.
        }
    }

}
