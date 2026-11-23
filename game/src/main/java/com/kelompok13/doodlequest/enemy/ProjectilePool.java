
package com.kelompok13.doodlequest.enemy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectilePool {

    private final List<Projectile> projectiles;

    public ProjectilePool(int initialSize) {

        if (initialSize < 0) {
            throw new IllegalArgumentException(
                    "Initial size cannot be negative"
            );
        }

        projectiles = new ArrayList<>();

        for (int i = 0; i < initialSize; i++) {
            projectiles.add(new Projectile());
        }
    }

    public Projectile obtain(
            double x,
            double y,
            double velocityX,
            double velocityY) {

        for (Projectile projectile : projectiles) {

            if (!projectile.isActive()) {
                projectile.activate(
                        x, y, velocityX, velocityY
                );

                return projectile;
            }
        }

        // Jika semua peluru sedang aktif,
        // tambahkan objek baru ke pool.
        Projectile projectile = new Projectile();
        projectile.activate(
                x, y, velocityX, velocityY
        );

        projectiles.add(projectile);

        return projectile;
    }

    public void release(Projectile projectile) {

        if (projectile == null ||
                !projectiles.contains(projectile)) {
            return;
        }

        projectile.deactivate();
    }

    public void update(double dt) {

        for (Projectile projectile : projectiles) {

            if (projectile.isActive()) {
                projectile.update(dt);
            }
        }
    }

    public List<Projectile> getActiveProjectiles() {

        List<Projectile> activeProjectiles =
                new ArrayList<>();

        for (Projectile projectile : projectiles) {

            if (projectile.isActive()) {
                activeProjectiles.add(projectile);
            }
        }

        return Collections.unmodifiableList(
                activeProjectiles
        );
    }

    public int getPoolSize() {
        return projectiles.size();
    }
}
