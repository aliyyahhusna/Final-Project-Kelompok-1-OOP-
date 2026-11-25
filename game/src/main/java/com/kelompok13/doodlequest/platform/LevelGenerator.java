
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Mengatur pembuatan platform pada setiap level.
public class LevelGenerator {

    private final Random random = new Random();

    // Membuat platform awal sesuai pengaturan level.
    public List<Platform> generate(
            LevelConfig config,
            double startY,
            int platformCount
    ) {

        List<Platform> platforms = new ArrayList<>();

        if (platformCount <= 0) {
            return platforms;
        }

        List<PlatformType> availableTypes = new ArrayList<>(
                config.getAllowedPlatforms()
        );

        double maxX = Math.max(
                0,
                Constants.WIDTH - Constants.PLATFORM_WIDTH
        );

        for (int i = 0; i < platformCount; i++) {

            // Nilai Y semakin kecil berarti platform semakin tinggi.
            double y = startY -
                    (i * config.getVerticalSpacing());

            // Posisi horizontal platform dibuat acak.
            double x = random.nextDouble() * maxX;

            PlatformType type;

            // Platform pertama dibuat normal sebagai pijakan awal.
            if (i == 0) {
                type = PlatformType.NORMAL;
            } else {
                type = availableTypes.get(
                        random.nextInt(availableTypes.size())
                );
            }

            platforms.add(
                    PlatformFactory.create(type, x, y)
            );
        }

        return platforms;
    }

    // Membuat platform tambahan di atas platform terakhir.
    public List<Platform> generateNext(
            LevelConfig config,
            double lastPlatformY,
            int platformCount
    ) {

        List<Platform> platforms = new ArrayList<>();

        if (platformCount <= 0) {
            return platforms;
        }

        List<PlatformType> availableTypes = new ArrayList<>(
                config.getAllowedPlatforms()
        );

        double maxX = Math.max(
                0,
                Constants.WIDTH - Constants.PLATFORM_WIDTH
        );

        for (int i = 0; i < platformCount; i++) {

            // Mulai dari satu jarak di atas platform sebelumnya.
            double y = lastPlatformY -
                    ((i + 1) * config.getVerticalSpacing());

            double x = random.nextDouble() * maxX;

            PlatformType type = availableTypes.get(
                    random.nextInt(availableTypes.size())
            );

            platforms.add(
                    PlatformFactory.create(type, x, y)
            );
        }

        return platforms;
    }

    // Menghapus platform yang sudah tidak aktif.
    public void removeInactivePlatforms(List<Platform> platforms) {
        platforms.removeIf(platform -> !platform.isActive());
    }
}
