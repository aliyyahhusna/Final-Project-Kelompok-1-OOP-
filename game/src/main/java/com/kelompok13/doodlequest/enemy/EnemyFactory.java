
package com.kelompok13.doodlequest.enemy;

public class EnemyFactory {

    public static Enemy createEnemy(
            EnemyType type,
            double x,
            double y) {

        if (type == null) {
            throw new IllegalArgumentException(
                    "Enemy type cannot be null"
            );
        }

        switch (type) {

            case STATIONARY:
                return new StationaryEnemy(x, y);

            case MOVING:
                return new MovingEnemy(x, y);

            default:
                throw new IllegalArgumentException(
                        "Unknown enemy type: " + type
                );
        }
    }
}
