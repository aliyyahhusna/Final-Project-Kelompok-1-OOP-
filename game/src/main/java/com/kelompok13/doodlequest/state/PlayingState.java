
package com.kelompok13.doodlequest.state;

import com.kelompok13.doodlequest.common.Constants;
import com.kelompok13.doodlequest.core.Camera2D;
import com.kelompok13.doodlequest.core.GameManager;
import com.kelompok13.doodlequest.platform.LevelConfig;
import com.kelompok13.doodlequest.platform.LevelGenerator;
import com.kelompok13.doodlequest.platform.Platform;
import com.kelompok13.doodlequest.platform.PlatformFactory;
import com.kelompok13.doodlequest.platform.PlatformType;
import com.kelompok13.doodlequest.player.CollisionSystem;
import com.kelompok13.doodlequest.player.Player;
import com.kelompok13.doodlequest.ui.Hud;
import com.kelompok13.doodlequest.ui.ParallaxBackground;
import com.kelompok13.doodlequest.enemy.Enemy;
import com.kelompok13.doodlequest.enemy.EnemyFactory;
import com.kelompok13.doodlequest.enemy.EnemyType;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public final class PlayingState extends GameState {

    private final GameManager game = GameManager.getInstance();
    private final Hud hud = new Hud();
    private final Camera2D camera = new Camera2D();

    private final ParallaxBackground level1Background =
            new ParallaxBackground();

    // Menghubungkan generator dengan daftar platform di gameplay.
    private final LevelGenerator generator = new LevelGenerator();
    private final List<Platform> platforms = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private double nextEnemyY;
    private int spawnedEnemyCount;

    // Menggunakan player dan collision system yang sudah tersedia.
    private final Player player = new Player(580, 500);
    private final CollisionSystem collision = new CollisionSystem();

    private LevelConfig levelConfig;
    private double lastPlatformY;

    private double heightThisLevel = 0;
    private double levelStartY = 500;
    private double highestWorldY = 500;
    private double previousLevelHeight = 0;


    private int scoredSteps = 0;

    private boolean transitioning = false;
    private boolean bossZone = false;
    private double transitionSeconds = 0;
    private int nextLevel = 1;

    public PlayingState(StateManager manager, boolean restart) {
        super(manager);

        if (restart) {
            game.reset();
        }

        setupLevel();
    }

    // Menyiapkan posisi player dan platform untuk level aktif.
    private void setupLevel() {

        camera.reset();
        platforms.clear();
        enemies.clear();
        nextEnemyY = 100;
        spawnedEnemyCount = 0;

        if (game.getLevel() == 1) {
            enemies.add(EnemyFactory.createEnemy(
                    EnemyType.STATIONARY, 280, 420
            ));

            enemies.add(EnemyFactory.createEnemy(
                    EnemyType.MOVING, 850, 390
            ));
        }

        levelConfig = LevelConfig.getConfig(game.getLevel());

        // Menyiapkan pijakan aman untuk awal permainan.
        Platform startingPlatform = PlatformFactory.create(
                PlatformType.NORMAL,
                580,
                580
        );

        platforms.add(startingPlatform);

        // Membuat platform awal di atas pijakan pertama.
        List<Platform> initialPlatforms =
                generator.generate(levelConfig, 480, 12);

        platforms.addAll(initialPlatforms);

        // Menyimpan posisi platform paling atas yang sudah dibuat.
        lastPlatformY = 480 -
                (11 * levelConfig.getVerticalSpacing());

        // Player ditempatkan tepat di atas platform awal.
        player.spawnAt(580, 580);
        player.bounce();

        levelStartY = player.getY();
        highestWorldY = levelStartY;
        heightThisLevel = 0;
        scoredSteps = 0;
        bossZone = false;
    }

    @Override
    public void onEnter() {
        game.addObserver(hud);

        hud.onGameUpdated(
                game.getLevel(),
                game.getLives(),
                game.getScore(),
                game.getMaxHeight()
        );
    }

    @Override
    public void onExit() {
        game.removeObserver(hud);
    }

    @Override
    public void update(double dt) {

        // Menghentikan gameplay sementara selama pergantian level.
        if (transitioning) {

            transitionSeconds -= dt;

            if (transitionSeconds <= 0) {

                transitioning = false;

                previousLevelHeight += heightThisLevel;
                game.setLevel(nextLevel);

                setupLevel();
            }

            return;
        }

        // Memperbarui gerakan player.
        player.update(dt);

        // Memperbarui semua platform, termasuk moving platform.
        for (Platform platform : platforms) {
            platform.update(dt);
        }
        for (Enemy enemy : enemies) {
            enemy.update(dt);
        }

        // Mengecek apakah player mendarat pada platform.
        collision.checkPlatforms(player, platforms);
        int livesBeforeEnemy = game.getLives();
        collision.checkEnemies(player, enemies);
        if (game.getLives() < livesBeforeEnemy) {

            if (game.getLives() <= 0) {
                manager.change(new GameOverState(manager));
                return;
            }
            respawnPlayer();
            return;
        }

        // Kamera mengikuti posisi player saat bergerak ke atas.
        camera.follow(player.getY());
        if (game.getLevel() == 1) {

            int defeatedCount = 0;

            for (Enemy enemy : enemies) {
                if (!enemy.isActive()) {
                    defeatedCount++;
                }
            }

            enemies.removeIf(enemy ->
                    !enemy.isActive()
                            || enemy.getY() >
                            camera.getY() + Constants.HEIGHT + 200
            );

            // Mengganti Enemy yang hilang
            for (int i = 0; i < defeatedCount; i++) {
                spawnNextEnemy();
            }

            // Memunculkan Enemy baru saat kamera naik
            while (nextEnemyY > camera.getY() - 350) {
                spawnNextEnemy();
            }
        }

        // Menyimpan posisi tertinggi yang sudah dicapai player.
        highestWorldY = Math.min(
                highestWorldY,
                player.getY()
        );

        heightThisLevel = Math.max(
                0,
                levelStartY - highestWorldY
        );

        // Tinggi total tetap dihitung dari level sebelumnya.
        game.setMaxHeight(
                previousLevelHeight + heightThisLevel
        );

        // Skor bertambah setiap kenaikan 100 unit.
        int currentSteps = (int) (heightThisLevel / 100);

        if (currentSteps > scoredSteps) {
            game.addScore((currentSteps - scoredSteps) * 10);
            scoredSteps = currentSteps;
        }

        // Menambah platform baru ketika kamera bergerak naik.
        extendPlatforms();

        // Menghapus platform yang sudah tidak dibutuhkan.
        platforms.removeIf(platform ->
                !platform.isActive() ||
                        platform.getY() >
                                camera.getY() + Constants.HEIGHT + 300
        );

        // Mengurangi nyawa ketika player jatuh melewati layar.
        boolean fell = collision.checkFall(
                player,
                camera.getY() + Constants.HEIGHT
        );

        if (fell) {

            if (game.getLives() <= 0) {
                manager.change(new GameOverState(manager));
                return;
            }

            respawnPlayer();
            return;
        }

        if (game.getLives() <= 0) {
            manager.change(new GameOverState(manager));
            return;
        }

        // Perpindahan level berdasarkan ketinggian player.
        if (game.getLevel() == 1 &&
                heightThisLevel >= levelConfig.getTargetHeight()) {

            startTransition(2);

        } else if (game.getLevel() == 2 &&
                heightThisLevel >= levelConfig.getTargetHeight()) {

            startTransition(3);

        } else if (game.getLevel() == 3 &&
                heightThisLevel >= levelConfig.getTargetHeight()) {

            bossZone = true;
        }
    }
    // Membuat Enemy baru di ketinggian berikutnya.
    private void spawnNextEnemy() {

        if (game.getLevel() != 1) {
            return;
        }

        // Jenis musuh muncul bergantian.
        EnemyType type = spawnedEnemyCount % 2 == 0
                ? EnemyType.STATIONARY
                : EnemyType.MOVING;

        // Posisi horizontal bergantian.
        double x = spawnedEnemyCount % 2 == 0
                ? 280
                : 850;

        enemies.add(
                EnemyFactory.createEnemy(type, x, nextEnemyY)
        );

        // Musuh berikutnya muncul 320 unit lebih tinggi.
        nextEnemyY -= 320;

        spawnedEnemyCount++;
    }
    // Membuat platform tambahan di atas platform terakhir.
    private void extendPlatforms() {

        double upperLimit = camera.getY() - 300;

        while (lastPlatformY > upperLimit) {

            List<Platform> newPlatforms =
                    generator.generateNext(
                            levelConfig,
                            lastPlatformY,
                            8
                    );

            platforms.addAll(newPlatforms);

            // Posisi terakhir diperbarui agar tidak mengulang platform.
            lastPlatformY -=
                    8 * levelConfig.getVerticalSpacing();
        }
    }

    // Menyiapkan pijakan baru setelah player kehilangan nyawa.
    private void respawnPlayer() {

        double safeY =
                camera.getY() + Constants.HEIGHT - 100;

        // Menambah platform normal untuk tempat respawn.
        platforms.add(
                PlatformFactory.create(
                        PlatformType.NORMAL,
                        580,
                        safeY
                )
        );

        player.spawnAt(580, safeY);
        player.makeInvulnerable(Player.INVULNERABLE_SECONDS);
        player.bounce();
    }

    // Mengatur waktu transisi sebelum masuk level selanjutnya.
    private void startTransition(int targetLevel) {

        if (transitioning) {
            return;
        }

        nextLevel = targetLevel;
        transitioning = true;

        transitionSeconds = Constants.LEVEL_INTRO_SECONDS;

        player.stopMoving();
    }

    @Override
    public void onKeyPressed(KeyCode k) {

        if (k == KeyCode.P || k == KeyCode.ESCAPE) {
            manager.change(new PausedState(manager, this));
            return;
        }

        if (transitioning) {
            return;
        }

        if (k == KeyCode.A || k == KeyCode.LEFT) {
            player.setMovingLeft(true);
        }

        if (k == KeyCode.D || k == KeyCode.RIGHT) {
            player.setMovingRight(true);
        }

        // Tombol bantuan untuk menguji nyawa.
        if (k == KeyCode.H) {
            game.loseLife();
        }

        // Tombol pengujian kemenangan sementara.
        if (k == KeyCode.B && bossZone) {
            manager.change(new VictoryState(manager));
        }
    }

    @Override
    public void onKeyReleased(KeyCode k) {

        if (k == KeyCode.A || k == KeyCode.LEFT) {
            player.setMovingLeft(false);
        }

        if (k == KeyCode.D || k == KeyCode.RIGHT) {
            player.setMovingRight(false);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {

        int level = transitioning
                ? nextLevel
                : game.getLevel();

        // Menentukan warna background sesuai level.
        Color bg = level == 1
                ? Color.web("#a0d9f2")
                : level == 2
                ? Color.web("#c7eaf9")
                : Color.web("#9fb0dd");

        gc.setFill(bg);

        gc.fillRect(
                0,
                0,
                Constants.WIDTH,
                Constants.HEIGHT
        );

        // Background parallax sementara tersedia untuk Level 1.
        if (level == 1) {
            level1Background.draw(gc, camera.getY());
        }

        // Menggeser gambar objek sesuai posisi kamera.
        gc.save();
        gc.translate(0, -camera.getY());

        // Menampilkan seluruh platform yang masih aktif.
        for (Platform platform : platforms) {

            if (!platform.isActive()) {
                continue;
            }

            // Bentuk sementara jika sprite platform belum tersedia.
            gc.setFill(Color.web("#468a59"));

            gc.fillRoundRect(
                    platform.getX(),
                    platform.getY(),
                    platform.getWidth(),
                    platform.getHeight(),
                    12,
                    12
            );

            // Menggambar aset platform jika gambarnya tersedia.
            platform.draw(gc);
        }

        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                enemy.draw(gc);
            }
        }
        // Player digambar sesuai posisi di dunia permainan.
        player.draw(gc);

        gc.restore();

        // HUD tetap berada di posisi layar.
        hud.draw(gc);

        // Menampilkan layar transisi level.
        if (transitioning) {

            gc.setFill(Color.rgb(7, 20, 43, 0.78));

            gc.fillRect(
                    0,
                    0,
                    Constants.WIDTH,
                    Constants.HEIGHT
            );

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Poppins", 75));

            gc.fillText(
                    "LEVEL " + nextLevel,
                    490,
                    310
            );

            gc.setFont(Font.font("Poppins", 43));

            gc.fillText(
                    nextLevel == 2
                            ? "FROZEN PEAKS"
                            : "BEYOND THE SKY",
                    nextLevel == 2 ? 480 : 445,
                    382
            );

            gc.setFont(Font.font("Poppins", 25));

            gc.fillText(
                    "GET READY!",
                    555,
                    445
            );
        }
    }
}
