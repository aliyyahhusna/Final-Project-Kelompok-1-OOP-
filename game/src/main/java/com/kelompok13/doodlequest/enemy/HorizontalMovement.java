
package com.kelompok13.doodlequest.enemy;

public class HorizontalMovement implements MovementStrategy {

    private final double minX;
    private final double maxX;
    private final double speed;

    private int direction = 1;

    public HorizontalMovement(double minX, double maxX, double speed) {
        this.minX = minX;
        this.maxX = maxX;
        this.speed = speed;
    }

    @Override
    public void move(Enemy enemy, double dt) {

        if (!enemy.isActive() || dt <= 0) {
            return;
        }

        double newX = enemy.getX() + speed * direction * dt;

        if (newX >= maxX) {
            newX = maxX;
            direction = -1;
        } else if (newX <= minX) {
            newX = minX;
            direction = 1;
        }

        enemy.setPosition(newX, enemy.getY());
    }
}
