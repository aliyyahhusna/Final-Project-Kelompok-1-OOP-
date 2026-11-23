
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.common.Constants;

import java.util.Set;

// Menyimpan pengaturan yang digunakan pada setiap level.
public class LevelConfig {

    private final int levelNumber;
    private final String levelName;
    private final double targetHeight;
    private final double verticalSpacing;
    private final Set<PlatformType> allowedPlatforms;
    private final String difficulty;

    // Menyimpan semua pengaturan level saat objek dibuat.
    private LevelConfig(
            int levelNumber,
            String levelName,
            double targetHeight,
            double verticalSpacing,
            Set<PlatformType> allowedPlatforms,
            String difficulty
    ) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
        this.targetHeight = targetHeight;
        this.verticalSpacing = verticalSpacing;
        this.allowedPlatforms = Set.copyOf(allowedPlatforms);
        this.difficulty = difficulty;
    }

    // Mengambil pengaturan berdasarkan nomor level.
    public static LevelConfig getConfig(int level) {

        switch (level) {

            case 1:
                return new LevelConfig(
                        1,
                        "Green Valley",
                        Constants.LEVEL_1_HEIGHT,
                        100,
                        Set.of(
                                PlatformType.NORMAL,
                                PlatformType.MOVING
                        ),
                        "Easy"
                );

            case 2:
                return new LevelConfig(
                        2,
                        "Frozen Peaks",
                        Constants.LEVEL_2_HEIGHT,
                        85,
                        Set.of(
                                PlatformType.NORMAL,
                                PlatformType.MOVING,
                                PlatformType.BREAKABLE
                        ),
                        "Medium"
                );

            case 3:
                return new LevelConfig(
                        3,
                        "Beyond the Sky",
                        Constants.LEVEL_3_BOSS_HEIGHT,
                        70,
                        Set.of(
                                PlatformType.NORMAL,
                                PlatformType.MOVING,
                                PlatformType.BREAKABLE
                        ),
                        "Hard"
                );

            default:
                throw new IllegalArgumentException(
                        "Level tidak tersedia: " + level
                );
        }
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getLevelName() {
        return levelName;
    }

    public double getTargetHeight() {
        return targetHeight;
    }

    public double getVerticalSpacing() {
        return verticalSpacing;
    }

    public Set<PlatformType> getAllowedPlatforms() {
        return allowedPlatforms;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
