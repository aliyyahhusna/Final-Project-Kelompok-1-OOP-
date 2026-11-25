
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.common.Constants;
import com.kelompok13.doodlequest.player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MovingPlatform extends Platform {

    // Gambar khusus untuk platform yang bergerak.
    private static final Image PLATFORM_IMAGE = loadImage();

    // Kecepatan dan jarak gerak platform.
    private static final double MOVE_SPEED = 90;
    private static final double MOVE_DISTANCE = 120;

    private final double startX;
    private int direction = 1;

    public MovingPlatform(double x, double y) {
        super(x, y);

        // Posisi awal digunakan sebagai batas gerakan platform.
        this.startX = x;
    }

    // Mengambil gambar moving platform dari folder assets.
    private static Image loadImage() {
        var imageStream = MovingPlatform.class.getResourceAsStream(
                "/images/platforms/moving_platform.png"
        );

        if (imageStream == null) {
            return null;
        }

        return new Image(imageStream);
    }

    // Menggerakkan platform ke kiri dan kanan.
    @Override
    public void update(double dt) {
        if (!active) {
            return;
        }

        x += MOVE_SPEED * direction * dt;

        // Kalau sudah mencapai batas kanan, platform berbalik arah.
        if (x >= startX + MOVE_DISTANCE) {
            x = startX + MOVE_DISTANCE;
            direction = -1;
        }

        // Kalau sudah mencapai batas kiri, platform bergerak ke kanan lagi.
        if (x <= startX - MOVE_DISTANCE) {
            x = startX - MOVE_DISTANCE;
            direction = 1;
        }

        // Menjaga agar platform tidak keluar dari area permainan.
        if (x < 0) {
            x = 0;
            direction = 1;
        }

        if (x + width > Constants.WIDTH) {
            x = Constants.WIDTH - width;
            direction = -1;
        }
    }

    // Menampilkan gambar platform pada posisi terbarunya.
    @Override
    public void draw(GraphicsContext gc) {
        if (!active || PLATFORM_IMAGE == null) {
            return;
        }

        gc.drawImage(PLATFORM_IMAGE, x, y, width, height);
    }


    @Override
    public void onLanded(Player player) {
        // Player tetap memantul meskipun platform bergerak.
        player.bounce();
    }

}
