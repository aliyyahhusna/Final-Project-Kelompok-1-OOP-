
package com.kelompok13.doodlequest.enemy;

public class EnemyTest {

    public static void main(String[] args) {

        // TEST 1: Stationary Enemy
        Enemy stationary = EnemyFactory.createEnemy(
                EnemyType.STATIONARY, 100, 200
        );

        double initialX = stationary.getX();
        stationary.update(1.0);

        System.out.println("TEST 1 - Stationary Enemy");
        System.out.println("Posisi awal: " + initialX);
        System.out.println("Posisi akhir: " + stationary.getX());

        // TEST 2: Moving Enemy
        Enemy moving = EnemyFactory.createEnemy(
                EnemyType.MOVING, 300, 250
        );

        System.out.println("\nTEST 2 - Moving Enemy");
        System.out.println("Posisi awal: " + moving.getX());

        moving.update(1.0);

        System.out.println("Posisi setelah 1 detik: "
                + moving.getX());

        moving.update(1.0);

        System.out.println("Posisi setelah 2 detik: "
                + moving.getX());
        moving.update(1.0);

        System.out.println("Posisi setelah 3 detik: "
                + moving.getX());

        moving.update(1.0);

        System.out.println("Posisi setelah 4 detik: "
                + moving.getX());


        System.out.println("\nTEST 3 - Strategy Pattern");

        MovingEnemy strategyEnemy = new MovingEnemy(500, 300);

        strategyEnemy.update(1.0);
        System.out.println("Sebelum ganti strategy: "
                + strategyEnemy.getX());

        strategyEnemy.setMovementStrategy(
                new StationaryMovement()
        );

        strategyEnemy.update(1.0);
        System.out.println("Setelah ganti strategy: "
                + strategyEnemy.getX());

        System.out.println("\nTEST 4 - Boss Sky Guardian");

        Boss boss = new Boss(400, 200);

        System.out.println("Lebar Boss: " + boss.getWidth());
        System.out.println("Tinggi Boss: " + boss.getHeight());

        System.out.println("Lebar hitbox kepala: "
                + boss.getHeadBounds().getWidth());

        System.out.println("Tinggi hitbox kepala: "
                + boss.getHeadBounds().getHeight());

        System.out.println("Status awal: " + boss.isActive());

        boss.defeat();

        System.out.println("Status setelah defeat: "
                + boss.isActive());

        System.out.println("\nTEST 5 - Projectile Pool");

        ProjectilePool pool = new ProjectilePool(2);

        System.out.println("Pool awal: " + pool.getPoolSize());

        Projectile p1 = pool.obtain(100, 200, 50, 0);
        Projectile p2 = pool.obtain(150, 200, 0, -50);

        System.out.println("Peluru aktif: "
                + pool.getActiveProjectiles().size());

        pool.update(1.0);

        System.out.println("Posisi p1: " + p1.getX());
        System.out.println("Posisi p2: " + p2.getY());

        pool.release(p1);

        System.out.println("Peluru aktif setelah release: "
                + pool.getActiveProjectiles().size());

        Projectile p3 = pool.obtain(300, 300, 100, 0);

        System.out.println("Objek p1 dipakai ulang: "
                + (p1 == p3));

        System.out.println("Pool akhir: " + pool.getPoolSize());



    }
}
