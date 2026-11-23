
package com.kelompok13.doodlequest.platform;

// Factory untuk membuat platform sesuai jenis yang dibutuhkan.
public class PlatformFactory {

    // Membuat platform berdasarkan tipe yang dipilih.
    public static Platform create(PlatformType type, double x, double y) {

        switch (type) {

            case NORMAL:
                // Membuat platform biasa.
                return new NormalPlatform(x, y);

            case MOVING:
                // Membuat platform yang bergerak ke kiri dan kanan.
                return new MovingPlatform(x, y);

            case BREAKABLE:
                // Membuat platform yang hancur setelah diinjak.
                return new BreakablePlatform(x, y);

            default:
                throw new IllegalArgumentException(
                        "Jenis platform tidak tersedia: " + type
                );
        }
    }
}
