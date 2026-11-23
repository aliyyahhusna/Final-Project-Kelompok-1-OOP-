package com.kelompok13.doodlequest.player;

import com.kelompok13.doodlequest.core.GameManager;
import com.kelompok13.doodlequest.enemy.Boss;
import com.kelompok13.doodlequest.enemy.Enemy;
import com.kelompok13.doodlequest.enemy.EnemyFactory;
import com.kelompok13.doodlequest.enemy.EnemyType;
import com.kelompok13.doodlequest.enemy.ProjectilePool;

import java.util.List;

/**
 * Uji manual Player + CollisionSystem (tanpa JUnit, sama seperti EnemyTest).
 * Jalankan main() dari IntelliJ. Semua baris harus "OK".
 */
public class PlayerCollisionTest {

    private static final double DT = 1.0 / 60;
    private static int failed = 0;

    public static void main(String[] args) {
        GameManager game = GameManager.getInstance();
        CollisionSystem collision = new CollisionSystem(game);

        // 1. Gravitasi: pemain diam lama-lama jatuh
        Player p = new Player(500, 0);
        p.update(DT);
        check("gravitasi membuat pemain jatuh", p.isFalling() && p.getY() > 0);

        // 2. Bounce membuat pemain naik
        p.bounce();
        p.update(DT);
        check("bounce membuat pemain naik", !p.isFalling());

        // 3. Batas layar kiri/kanan
        p = new Player(1250, 0);
        p.setMovingRight(true);
        for (int i = 0; i < 60; i++) p.update(DT);
        check("tidak keluar batas kanan", p.getX() <= 1280 - p.getWidth());
        p.setMovingRight(false);
        p.setMovingLeft(true);
        for (int i = 0; i < 300; i++) p.update(DT);
        check("tidak keluar batas kiri", p.getX() >= 0);

        // 4. Stomp musuh dari atas
        game.reset();
        Enemy enemy = EnemyFactory.createEnemy(EnemyType.STATIONARY, 500, 400);
        p = fallOnto(500, 400, enemy, collision);
        check("stomp: musuh kalah", !enemy.isActive());
        check("stomp: pemain memantul", !p.isFalling());
        check("stomp: nyawa tetap 3", game.getLives() == 3);
        check("stomp: skor bertambah", game.getScore() == CollisionSystem.STOMP_SCORE);

        // 5. Ditabrak musuh dari samping -> nyawa -1, dan tidak berkali-kali
        game.reset();
        enemy = EnemyFactory.createEnemy(EnemyType.STATIONARY, 500, 400);
        p = new Player(470, 390);
        for (int i = 0; i < 10; i++) {
            collision.checkEnemies(p, List.of(enemy));
        }
        check("hit samping: nyawa berkurang tepat 1", game.getLives() == 2);
        check("hit samping: musuh tetap aktif", enemy.isActive());

        // 6. Projectile kena pemain -> nyawa -1, peluru kembali ke pool
        game.reset();
        ProjectilePool pool = new ProjectilePool(3);
        p = new Player(500, 400);
        pool.obtain(520, 420, 0, 0);
        collision.checkProjectiles(p, pool);
        check("projectile: nyawa berkurang", game.getLives() == 2);
        check("projectile: peluru di-release", pool.getActiveProjectiles().isEmpty());

        // 7. Boss: stomp kepala -> menang, tanpa kehilangan nyawa
        game.reset();
        Boss boss = new Boss(500, 300);
        double headCenterX = boss.getHeadBounds().getMinX()
                + boss.getHeadBounds().getWidth() / 2;
        p = new Player(headCenterX - 40, 150);
        boolean won = false;
        for (int i = 0; i < 120 && !won; i++) {
            p.update(DT);
            won = collision.checkBoss(p, boss);
        }
        check("boss: stomp kepala menang", won && !boss.isActive());
        check("boss: nyawa tetap 3", game.getLives() == 3);

        // 8. Boss: kena badan dari samping -> nyawa -1
        game.reset();
        boss = new Boss(500, 300);
        p = new Player(440, 400);
        collision.checkBoss(p, boss);
        check("boss: kena badan mengurangi nyawa", game.getLives() == 2 && boss.isActive());

        // 9. Jatuh keluar layar
        game.reset();
        p = new Player(500, 800);
        check("jatuh: terdeteksi", collision.checkFall(p, 720));
        check("jatuh: nyawa berkurang", game.getLives() == 2);

        System.out.println(failed == 0 ? "\nSEMUA TEST LULUS" : "\n" + failed + " TEST GAGAL");
    }

    /** Jatuhkan pemain dari atas ke target sampai collision terjadi. */
    private static Player fallOnto(double x, double topY, Enemy enemy, CollisionSystem collision) {
        Player p = new Player(x, topY - 200);
        for (int i = 0; i < 120 && enemy.isActive(); i++) {
            p.update(DT);
            collision.checkEnemies(p, List.of(enemy));
        }
        return p;
    }

    private static void check(String name, boolean ok) {
        System.out.println((ok ? "OK    " : "GAGAL ") + name);
        if (!ok) failed++;
    }
}