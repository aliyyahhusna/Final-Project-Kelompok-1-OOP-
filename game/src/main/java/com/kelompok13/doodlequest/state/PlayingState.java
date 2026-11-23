
package com.kelompok13.doodlequest.state;

import com.kelompok13.doodlequest.common.Constants;
import com.kelompok13.doodlequest.core.Camera2D;
import com.kelompok13.doodlequest.core.GameManager;
import com.kelompok13.doodlequest.ui.Hud;
import com.kelompok13.doodlequest.ui.ParallaxBackground;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class PlayingState extends GameState {

    private final GameManager game = GameManager.getInstance();

    private final Hud hud = new Hud();

    private final ParallaxBackground level1Background =
            new ParallaxBackground();

    private final Camera2D camera = new Camera2D();

    private double heightThisLevel = 0;
    private double playerWorldY = 490;
    private double levelStartY = 490;
    private double highestWorldY = 490;

    private boolean transitioning = false;
    private boolean bossZone = false;
    private double transitionSeconds = 0;
    private int nextLevel = 1;

    private boolean left = false;
    private boolean right = false;
    private double playerX = 580;

    public PlayingState(StateManager manager, boolean restart) {
        super(manager);

        if (restart) {
            game.reset();
            camera.reset();
        }
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

        if (transitioning) {

            transitionSeconds -= dt;

            if (transitionSeconds <= 0) {

                transitioning = false;
                game.setLevel(nextLevel);

                playerWorldY = 490;
                levelStartY = 490;
                highestWorldY = 490;
                heightThisLevel = 0;

                camera.reset();

                playerX = 580;
                bossZone = false;

                left = false;
                right = false;
            }

            return;
        }

        if (left) {
            playerX -= 360 * dt;
        }

        if (right) {
            playerX += 360 * dt;
        }

        playerX = Math.max(
                0,
                Math.min(
                        Constants.WIDTH - Constants.PLAYER_SIZE,
                        playerX
                )
        );

        if (game.getLevel() == 1 &&
                heightThisLevel >= Constants.LEVEL_1_HEIGHT) {

            startTransition(2);

        } else if (game.getLevel() == 2 &&
                heightThisLevel >= Constants.LEVEL_2_HEIGHT) {

            startTransition(3);

        } else if (game.getLevel() == 3 &&
                heightThisLevel >= Constants.LEVEL_3_BOSS_HEIGHT) {

            bossZone = true;
        }

        if (game.getLives() <= 0) {
            manager.change(new GameOverState(manager));
        }
    }

    private void startTransition(int targetLevel) {

        if (transitioning) {
            return;
        }

        nextLevel = targetLevel;
        transitioning = true;

        transitionSeconds = Constants.LEVEL_INTRO_SECONDS;

        left = false;
        right = false;
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
            left = true;
        }

        if (k == KeyCode.D || k == KeyCode.RIGHT) {
            right = true;
        }

        if (k == KeyCode.SPACE) {

            playerWorldY -= 100;

            camera.follow(playerWorldY);

            highestWorldY = Math.min(
                    highestWorldY,
                    playerWorldY
            );

            heightThisLevel = Math.max(
                    0,
                    levelStartY - highestWorldY
            );

            game.setMaxHeight(
                    game.getMaxHeight() + 100
            );

            game.addScore(10);
        }

        if (k == KeyCode.H) {
            game.loseLife();
        }

        if (k == KeyCode.B && bossZone) {
            manager.change(new VictoryState(manager));
        }
    }

    @Override
    public void onKeyReleased(KeyCode k) {

        if (k == KeyCode.A || k == KeyCode.LEFT) {
            left = false;
        }

        if (k == KeyCode.D || k == KeyCode.RIGHT) {
            right = false;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {

        int level = transitioning
                ? nextLevel
                : game.getLevel();

        Color bg = level == 1
                ? Color.web("#a0d9f2")
                : level == 2
                ? Color.web("#c7eaf9")
                : Color.web("#9fb0dd");

        gc.setFill(bg);

        gc.fillRect(
                0, 0,
                Constants.WIDTH,
                Constants.HEIGHT
        );

        if (level == 1) {
            level1Background.draw(gc, camera.getY());
        }

        gc.setFill(
                level == 2
                        ? Color.web("#7ac1da")
                        : Color.web("#468a59")
        );

        for (int i = 0; i < 6; i++) {

            double platformWorldY =
                    580 - (i % 3) * 125;

            double screenY =
                    camera.worldToScreenY(platformWorldY);

            gc.fillRoundRect(
                    140 + i * 185,
                    screenY,
                    Constants.PLATFORM_WIDTH,
                    Constants.PLATFORM_HEIGHT,
                    15, 15
            );
        }

        double playerScreenY =
                camera.worldToScreenY(playerWorldY);

        gc.setFill(Color.web("#4778c8"));

        gc.fillRoundRect(
                playerX,
                playerScreenY,
                Constants.PLAYER_SIZE,
                Constants.PLAYER_SIZE,
                18, 18
        );

        hud.draw(gc);

        if (transitioning) {

            gc.setFill(Color.rgb(7, 20, 43, 0.78));

            gc.fillRect(
                    0, 0,
                    Constants.WIDTH,
                    Constants.HEIGHT
            );

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Poppins", 75));

            gc.fillText(
                    "LEVEL " + nextLevel,
                    490, 310
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
                    555, 445
            );
        }
    }
}
