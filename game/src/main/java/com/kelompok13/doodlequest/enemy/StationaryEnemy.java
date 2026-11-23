
package com.kelompok13.doodlequest.enemy;

public class StationaryEnemy extends Enemy {

    private final MovementStrategy movementStrategy;

    public StationaryEnemy(double x, double y) {
        super(x, y, EnemyType.STATIONARY);

        this.movementStrategy = new StationaryMovement();
    }

    @Override
    public void update(double dt) {
        if (!isActive()) {
            return;
        }

        movementStrategy.move(this, dt);
    }
}
