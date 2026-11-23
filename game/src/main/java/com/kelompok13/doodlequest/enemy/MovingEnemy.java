
package com.kelompok13.doodlequest.enemy;

public class MovingEnemy extends Enemy {

    private MovementStrategy movementStrategy;

    public MovingEnemy(double x, double y) {
        super(x, y, EnemyType.MOVING);

        this.movementStrategy = new HorizontalMovement(
                x - 100,
                x + 100,
                70
        );
    }

    @Override
    public void update(double dt) {

        if (!isActive()) {
            return;
        }

        movementStrategy.move(this, dt);
    }

    public void setMovementStrategy(MovementStrategy strategy) {

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "MovementStrategy cannot be null"
            );
        }

        this.movementStrategy = strategy;
    }
}
