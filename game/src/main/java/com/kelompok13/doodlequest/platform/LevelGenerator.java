
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Mengatur pembuatan dan penempatan platform pada setiap level.
public class LevelGenerator {

    private final Random random = new Random();

    // Membuat kumpulan platform sesuai konfigurasi level.
    public List<Platform> generate(
            LevelConfig config,
            double startY,
            int platformCount
    ) {

        List<Platform> platforms = new ArrayList<>();

        // Mengambil jenis platform yang boleh muncul pada level ini.
        List<PlatformType> availableTypes = new ArrayList<>(
                config.getAllowedPlatforms()
        );

        for (int i = 0; i < platformCount; i++) {

            // Mengatur jarak vertikal antarplatform.
            double y = startY -
                    (i * config.getVerticalSpacing());

            // Posisi horizontal dibuat acak agar jalur tidak monoton.
            double maxX = Constants.WIDTH -
                    Constants.PLATFORM_WIDTH;

            double x = random.nextDouble() * maxX;

            PlatformType type;

            // Platform pertama selalu normal agar pemain punya pijakan awal.
            if (i == 0) {
                type = PlatformType.NORMAL;
            } else {
                // Memilih jenis platform yang tersedia pada level.
                type = availableTypes.get(
                        random.nextInt(availableTypes.size())
                );
            }

            // Membuat objek platform melalui PlatformFactory.
            Platform platform = PlatformFactory.create(
                    type,
                    x,
                    y
            );

            platforms.add(platform);
        }

        return platforms;
    }

    // Menghapus platform yang sudah tidak aktif dari daftar.
    public void removeInactivePlatforms(List<Platform> platforms) {
        platforms.removeIf(platform -> !platform.isActive());
    }
}
