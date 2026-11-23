package com.kelompok13.doodlequest.common;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {
    protected double x, y, width, height;
    protected boolean active = true;
    protected GameObject(double x, double y, double width, double height) {
        this.x=x; this.y=y; this.width=width; this.height=height;
    }
    public abstract void update(double dt);
    public abstract void draw(GraphicsContext gc);
    public Rectangle2D getBounds() { return new Rectangle2D(x, y, width, height); }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public boolean isActive() { return active; }
}
