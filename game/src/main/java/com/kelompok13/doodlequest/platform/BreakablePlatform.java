
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BreakablePlatform extends Platform {

    // Gambar khusus untuk platform yang bisa hancur.
    private static final Image PLATFORM_IMAGE = loadImage();

    public BreakablePlatform(double x, double y) {
        super(x, y);
    }

    // Mengambil gambar breakable platform dari folder assets.
    private static Image loadImage() {
        var imageStream = BreakablePlatform.class.getResourceAsStream(
                "/images/platforms/breakable_platform.png"
        );

        if (imageStream == null) {
            return null;
        }

        return new Image(imageStream);
    }

    // Platform tidak bergerak selama masih aktif.
    @Override
    public void update(double dt) {
        // Tidak ada pergerakan pada platform ini.
    }

    // Menampilkan platform selama belum hancur.
    @Override
    public void draw(GraphicsContext gc) {
        if (!active || PLATFORM_IMAGE == null) {
            return;
        }

        gc.drawImage(PLATFORM_IMAGE, x, y, width, height);
    }

    // Platform akan hancur setelah player berhasil mendarat.
    @Override
    public void onLanded(Player player) {
        if (!active) {
            return;
        }

        active = false;
    }
}
