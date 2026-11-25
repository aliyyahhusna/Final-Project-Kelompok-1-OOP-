
package com.kelompok13.doodlequest.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public final class LoginScreen {

    public void draw(
            GraphicsContext gc,
            String username,
            String password,
            int selectedField,
            boolean registerMode,
            boolean busy,
            String message
    ) {
        gc.setFill(Color.web("#87c9ef"));
        gc.fillRect(0, 0, 1280, 720);

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 65));
        gc.setFill(Color.web("#173b59"));
        gc.fillText("DOODLE QUEST", 640, 125);

        gc.setFont(Font.font("Poppins", 28));
        gc.fillText(
                registerMode ? "CREATE ACCOUNT" : "WELCOME BACK!",
                640, 185
        );

        gc.setFill(Color.rgb(16, 54, 81, 0.85));
        gc.fillRoundRect(340, 220, 600, 390, 30, 30);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 34));
        gc.fillText(registerMode ? "REGISTER" : "LOGIN", 640, 275);

        drawField(
                gc, "USERNAME", username,
                410, 315, selectedField == 0, false
        );

        drawField(
                gc, "PASSWORD", password,
                410, 405, selectedField == 1, true
        );

        gc.setFill(Color.web("#4a9b6b"));
        gc.fillRoundRect(430, 510, 420, 55, 16, 16);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Poppins", FontWeight.BOLD, 23));
        gc.fillText(
                busy ? "PLEASE WAIT..." :
                        registerMode ? "REGISTER" : "LOGIN",
                640, 546
        );


        gc.setFont(Font.font("Poppins", 15));
        gc.fillText(
                "↑ ↓ : SWITCH FIELD   |   ENTER: SUBMIT",
                640, 655
        );

        gc.fillText(
                "F2: LOGIN / REGISTER   |   ESC: BACK",
                640, 685
        );

        gc.setTextAlign(TextAlignment.LEFT);
    }


    private void drawField(
            GraphicsContext gc,
            String label,
            String value,
            double x,
            double y,
            boolean selected,
            boolean passwordField
    ) {
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(x, y, 460, 65, 12, 12);

        if (selected) {
            gc.setStroke(Color.web("#f5bd45"));
            gc.setLineWidth(3);
            gc.strokeRoundRect(x, y, 460, 65, 12, 12);
        }

        gc.setTextAlign(TextAlignment.LEFT);

        gc.setFill(Color.web("#24486d"));
        gc.setFont(Font.font("Poppins", 12));
        gc.fillText(label, x + 15, y + 18);

        gc.setFont(Font.font("Poppins", 19));

        String display = passwordField
                ? "*".repeat(value.length())
                : value;

        gc.fillText(display, x + 15, y + 48);

        gc.setTextAlign(TextAlignment.CENTER);
    }

}
