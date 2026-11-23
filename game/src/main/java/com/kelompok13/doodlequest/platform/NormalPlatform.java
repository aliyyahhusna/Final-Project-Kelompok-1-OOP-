
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NormalPlatform extends Platform {

    // Mengambil gambar platform dari folder resources.
    private static final Image PLATFORM_IMAGE = loadImage();

    public NormalPlatform(double x, double y) {
        super(x, y);
    }

    // Memuat aset gambar platform.
    private static Image loadImage() {
        var imageStream = NormalPlatform.class.getResourceAsStream(
                "/images/platforms/normal_platform.png"
        );

        if (imageStream == null) {
            return null;
        }

        return new Image(imageStream);
    }

    // Platform biasa tidak memiliki gerakan khusus.
    @Override
    public void update(double dt) {
        // Posisi platform tetap.
    }

    // Menampilkan aset platform pada posisi yang sudah ditentukan.
    @Override
    public void draw(GraphicsContext gc) {
        if (PLATFORM_IMAGE != null) {
            gc.drawImage(PLATFORM_IMAGE, x, y, width, height);
        }
    }

    // Tidak ada efek tambahan ketika player mendarat.
    @Override
    public void onLanded(Player player) {
        // Player tetap menggunakan mekanisme pantulan normal.
    }
}
