package com.kelompok13.doodlequest.core;

public class Camera2D {

    private double y = 0;

    private static final double FOLLOW_Y = 280;

    public void follow(double playerWorldY) {
        double targetY = playerWorldY - FOLLOW_Y;

        y = Math.min(y, targetY);
    }

    public double worldToScreenY(double worldY) {
        return worldY - y;
    }

    public double getY() {
        return y;
    }

    public void reset() {
        y = 0;
    }
}
