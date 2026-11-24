
package com.kelompok13.doodlequest.state;

import com.kelompok13.doodlequest.common.Constants;
import com.kelompok13.doodlequest.ui.ParallaxBackground;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.FontWeight;

public final class MenuState extends GameState {

    private final ParallaxBackground background =
            new ParallaxBackground();

    private double animationTime = 0;

    private int selectedButton = 0;

    private static final double CENTER_X = Constants.WIDTH / 2.0;

    public MenuState(StateManager manager) {
        super(manager);
    }

    @Override
    public void update(double dt) {
        animationTime += dt;
    }

    @Override
    public void draw(GraphicsContext gc) {

        background.draw(gc, 0);

        gc.setFill(Color.rgb(245, 250, 255, 0.18));
        gc.fillRect(
                0, 0,
                Constants.WIDTH,
                Constants.HEIGHT
        );

        double floatingY =
                Math.sin(animationTime * 1.6) * 7;

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 78));
        gc.setFill(Color.web("#ffffff"));
        gc.fillText(
                "DOODLE QUEST",
                CENTER_X + 4,
                190 + floatingY + 4
        );

        gc.setFill(Color.web("#173b59"));
        gc.fillText(
                "DOODLE QUEST",
                CENTER_X,
                190 + floatingY
        );

        gc.setFont(Font.font("Poppins", 30));
        gc.setFill(Color.web("#ffffff"));
        gc.fillText(
                "✦ BEYOND THE SKY ✦",
                CENTER_X,
                245 + floatingY
        );

        gc.setFill(Color.rgb(16, 54, 81, 0.48));

        gc.fillRoundRect(
                CENTER_X - 245,
                307,
                490,
                290,
                34,
                34
        );

        drawButton(
                gc,
                "START GAME",
                365,
                selectedButton == 0,
                Color.web("#4a9b6b")
        );

        drawButton(
                gc,
                "LOGIN",
                460,
                selectedButton == 1,
                Color.web("#6a91bc")
        );

        // 7. Petunjuk keyboard
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Poppins", 20));
        gc.fillText(
                "↑ ↓ PILIH   •   ENTER KONFIRMASI",
                CENTER_X,
                565
        );


        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void drawButton(
            GraphicsContext gc,
            String text,
            double y,
            boolean selected,
            Color color
    ) {

        double width = selected ? 370 : 350;
        double height = selected ? 72 : 66;

        double x = CENTER_X - width / 2;
        double topY = y - height / 2;

        // Bayangan tombol
        gc.setFill(Color.rgb(10, 36, 56, 0.35));

        gc.fillRoundRect(
                x + 5,
                topY + 6,
                width,
                height,
                22,
                22
        );

        gc.setFill(color);

        gc.fillRoundRect(
                x,
                topY,
                width,
                height,
                22,
                22
        );

        if (selected) {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);

            gc.strokeRoundRect(
                    x,
                    topY,
                    width,
                    height,
                    22,
                    22
            );
        }

        gc.setFill(Color.WHITE);

        gc.setFont(
                Font.font("Poppins", selected ? 29 : 27)
        );

        gc.fillText(
                text,
                CENTER_X,
                y + 10
        );
    }

    @Override
    public void onKeyPressed(KeyCode key) {

        if (key == KeyCode.UP || key == KeyCode.W) {
            selectedButton = 0;
        }

        if (key == KeyCode.DOWN || key == KeyCode.S) {
            selectedButton = 1;
        }

        if (key == KeyCode.ENTER) {

            if (selectedButton == 0) {
                manager.change(
                        new PlayingState(manager, true)
                );
            } else {
                manager.change(
                        new LoginState(manager)
                );
            }

            return;
        }

        if (key == KeyCode.L) {
            manager.change(
                    new LoginState(manager)
            );
        }
    }
}
