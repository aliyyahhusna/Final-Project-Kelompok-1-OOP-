package com.kelompok13.doodlequest.player;

import com.kelompok13.doodlequest.core.GameManager;
import com.kelompok13.doodlequest.enemy.Boss;
import com.kelompok13.doodlequest.enemy.Enemy;
import com.kelompok13.doodlequest.enemy.Projectile;
import com.kelompok13.doodlequest.enemy.ProjectilePool;
import com.kelompok13.doodlequest.platform.Platform;
import javafx.geometry.Rectangle2D;

import java.util.List;

/**
 * Satu-satunya tempat yang memutuskan hasil tabrakan dengan pemain:
 * mendarat, stomp, kena hit, dan jatuh keluar layar.
 *
 * Pengurangan nyawa HANYA terjadi di sini (lewat GameManager.loseLife()),
 * supaya tidak ada double damage dari modul lain.
 *
 * Urutan pemanggilan per frame di PlayingState (saran):
 *   player.update(dt); platforms/enemies/boss/pool.update(dt);
 *   collision.checkPlatforms(...); collision.checkEnemies(...);
 *   collision.checkProjectiles(...); collision.checkBoss(...); collision.checkFall(...);
 */
public class CollisionSystem {

    public static final int STOMP_SCORE = 50;

    private final GameManager game;

    public CollisionSystem() {
        this(GameManager.getInstance());
    }

    public CollisionSystem(GameManager game) {
        this.game = game;
    }

    /**
     * Landing hanya sah kalau pemain sedang jatuh dan kakinya di frame
     * sebelumnya ada di atas permukaan platform (tidak memantul dari bawah).
     * Efek khusus (bounce biasa, breakable hancur, dll.) ditentukan
     * Platform.onLanded(player) milik Aliyyah.
     */
    public void checkPlatforms(Player player, List<? extends Platform> platforms) {
        if (platforms == null) {
            return;
        }

        Platform landed = null;
        for (Platform platform : platforms) {
            if (platform == null || !platform.isActive()) {
                continue;
            }
            if (isLandingOn(player, platform.getBounds())) {
                // Kalau dua platform bertumpuk, ambil yang permukaannya paling atas.
                if (landed == null || platform.getY() < landed.getY()) {
                    landed = platform;
                }
            }
        }

        if (landed != null) {
            player.landOn(landed.getY());
            landed.onLanded(player);
        }
    }

    /** Stomp dari atas = musuh kalah + bounce + skor. Selain itu = pemain kena hit. */
    public void checkEnemies(Player player, List<? extends Enemy> enemies) {
        if (enemies == null) {
            return;
        }

        for (Enemy enemy : enemies) {
            if (enemy == null || !enemy.isActive()) {
                continue;
            }

            Rectangle2D bounds = enemy.getBounds();

            boolean touching = isLandingOn(player, bounds)
                    || player.getBounds().intersects(bounds);

            if (touching && player.takeHit()) {
                game.loseLife();
                enemy.defeat();
            }
        }
    }


    /** Peluru yang mengenai pemain selalu dikembalikan ke pool. */
    public void checkProjectiles(Player player, ProjectilePool pool) {
        if (pool == null) {
            return;
        }

        // getActiveProjectiles() mengembalikan salinan, aman di-release saat iterasi.
        for (Projectile projectile : pool.getActiveProjectiles()) {
            if (player.getBounds().intersects(projectile.getBounds())) {
                pool.release(projectile);
                damage(player);
            }
        }
    }

    /**
     * Boss hanya kalah lewat stomp di KEPALA (hitbox kepala lebih kecil dari badan).
     * Mengembalikan true jika boss baru saja dikalahkan -> PlayingState pindah ke VictoryState.
     */
    public boolean checkBoss(Player player, Boss boss) {
        if (boss == null || !boss.isActive()) {
            return false;
        }

        Rectangle2D playerBounds = player.getBounds();
        Rectangle2D head = boss.getHeadBounds();

        boolean overHead = playerBounds.getMaxX() > head.getMinX()
                && playerBounds.getMinX() < head.getMaxX();
        boolean comingFromAbove = player.isFalling()
                && player.getPreviousBottom() <= head.getMaxY();

        if (overHead && comingFromAbove) {
            if (playerBounds.intersects(head)) {
                boss.defeat();
                player.landOn(head.getMinY());
                player.bounce();
                return true;
            }
            // Sedang turun ke arah kepala: jangan dihitung kena badan dulu.
            return false;
        }

        if (playerBounds.intersects(boss.getBounds())) {
            damage(player);
        }
        return false;
    }

    /**
     * Pemain jatuh melewati batas bawah layar.
     * cameraBottomWorldY = camera.getY() + Constants.HEIGHT.
     * Mengembalikan true jika pemain jatuh; PlayingState lalu memanggil
     * player.spawnAt(...) di platform aman, atau pindah ke GameOver bila nyawa habis.
     */
    public boolean checkFall(Player player, double cameraBottomWorldY) {
        if (player.getY() > cameraBottomWorldY) {
            game.loseLife();
            player.makeInvulnerable(Player.INVULNERABLE_SECONDS);
            return true;
        }
        return false;
    }

    private void damage(Player player) {
        if (player.takeHit()) {
            game.loseLife();
        }
    }

    private boolean isLandingOn(Player player, Rectangle2D target) {
        if (!player.isFalling()) {
            return false;
        }

        Rectangle2D p = player.getBounds();
        boolean horizontalOverlap = p.getMaxX() > target.getMinX()
                && p.getMinX() < target.getMaxX();
        boolean wasAbove = player.getPreviousBottom() <= target.getMinY();
        boolean nowAtOrBelowTop = player.getBottom() >= target.getMinY();

        return horizontalOverlap && wasAbove && nowAtOrBelowTop;
    }
}