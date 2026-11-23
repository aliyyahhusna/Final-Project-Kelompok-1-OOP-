
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.common.Constants;
import com.kelompok13.doodlequest.common.GameObject;
import com.kelompok13.doodlequest.player.Player;

// Class dasar yang menjadi induk semua jenis platform.
public abstract class Platform extends GameObject {

    // Constructor untuk menentukan posisi dan ukuran platform.
    protected Platform(double x, double y) {
        super(
                x,
                y,
                Constants.PLATFORM_WIDTH,
                Constants.PLATFORM_HEIGHT
        );
    }

    // Efek yang dijalankan ketika player berhasil mendarat.
    public abstract void onLanded(Player player);
}
