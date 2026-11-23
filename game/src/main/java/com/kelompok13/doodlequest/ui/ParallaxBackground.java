package com.kelompok13.doodlequest.ui;

import com.kelompok13.doodlequest.common.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class ParallaxBackground {

    private final Image[] layers = new Image[6];

    private final double[] speeds = {
            0.0,
            0.05,
            0.10,
            0.20,
            0.35,
            0.50
    };

    public ParallaxBackground() {

        String folder = "/images/backgrounds/level1/";

        String[] files = {
                "00_sky.png",
                "01_clouds.png",
                "02_far.png",
                "03_mid.png",
                "04_near.png",
                "05_ground.png"
        };

        for (int i = 0; i < files.length; i++) {

            layers[i] = new Image(
                    Objects.requireNonNull(
                            getClass().getResourceAsStream(
                                    folder + files[i]
                            ),
                            "Gambar tidak ditemukan: " + files[i]
                    )
            );
        }
    }

    private void drawLayer(
            GraphicsContext gc,
            Image image,
            double y
    ) {

        double screenW = Constants.WIDTH;
        double screenH = Constants.HEIGHT;

        double imageW = image.getWidth();
        double imageH = image.getHeight();

        double targetRatio = screenW / screenH;
        double imageRatio = imageW / imageH;

        double sourceX = 0;
        double sourceY = 0;
        double sourceW = imageW;
        double sourceH = imageH;

        if (imageRatio > targetRatio) {
            sourceW = imageH * targetRatio;
            sourceX = (imageW - sourceW) / 2;
        } else {
            sourceH = imageW / targetRatio;
            sourceY = (imageH - sourceH) / 2;
        }

        gc.drawImage(
                image,
                sourceX, sourceY,
                sourceW, sourceH,
                0, y,
                screenW, screenH
        );
    }

    public void draw(GraphicsContext gc, double cameraY) {

        drawLayer(gc, layers[0], 0);

        for (int i = 1; i < layers.length; i++) {

            double offsetY = -cameraY * speeds[i];

            if (offsetY >= Constants.HEIGHT) {
                continue;
            }

            drawLayer(gc, layers[i], offsetY);
        }
    }
}
