
package com.kelompok13.doodlequest.state;

import com.kelompok13.doodlequest.service.ApiAuthService;
import com.kelompok13.doodlequest.service.AuthSession;
import com.kelompok13.doodlequest.ui.LoginScreen;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.concurrent.CompletionException;

public final class LoginState extends GameState {

    private final LoginScreen view = new LoginScreen();
    private final ApiAuthService auth = new ApiAuthService();

    private final StringBuilder username = new StringBuilder();
    private final StringBuilder password = new StringBuilder();

    private int selectedField = 0;

    private boolean registerMode = false;
    private boolean busy = false;

    private String message = "Enter your username and password";

    public LoginState(StateManager manager) {
        super(manager);
    }

    @Override
    public void update(double dt) {
    }

    @Override
    public void draw(GraphicsContext gc) {
        view.draw(
                gc,
                username.toString(),
                password.toString(),
                selectedField,
                registerMode,
                busy,
                message
        );
    }

    @Override
    public void onKeyTyped(String character) {

        if (busy || character == null) return;

        if (character.length() != 1) return;

        char c = character.charAt(0);

        if (Character.isISOControl(c)) return;

        if (selectedField == 0 && username.length() < 20) {
            username.append(c);
        } else if (selectedField == 1 && password.length() < 72) {
            password.append(c);
        }
    }

    @Override
    public void onKeyPressed(KeyCode k) {

        if (k == KeyCode.ESCAPE) {
            if (!busy) {
                manager.change(new MenuState(manager));
            }
            return;
        }

        if (busy) return;

        if (k == KeyCode.UP) {
            selectedField = 0;
            return;
        }

        if (k == KeyCode.DOWN) {
            selectedField = 1;
            return;
        }

        if (k == KeyCode.BACK_SPACE) {
            StringBuilder target =
                    selectedField == 0 ? username : password;

            if (!target.isEmpty()) {
                target.deleteCharAt(target.length() - 1);
            }
            return;
        }

        if (k == KeyCode.F2) {
            registerMode = !registerMode;
            message = registerMode
                    ? "Create your account"
                    : "Welcome back!";
            return;
        }

        if (k == KeyCode.ENTER) {
            submit();
        }
    }

    private void submit() {

        String user = username.toString().trim();
        String pass = password.toString();

        if (user.isBlank() || pass.isBlank()) {
            message = "Username and password are required";
            return;
        }

        busy = true;
        message = registerMode
                ? "Registering account..."
                : "Logging in...";

        if (registerMode) {

            auth.register(user, pass)
                    .whenComplete((result, error) ->
                            Platform.runLater(() -> {

                                busy = false;

                                if (error != null) {
                                    message = getErrorMessage(error);
                                } else {
                                    registerMode = false;
                                    password.setLength(0);
                                    message = "Registration successful! Please login.";
                                }
                            })
                    );

        } else {

            auth.login(user, pass)
                    .whenComplete((result, error) ->
                            Platform.runLater(() -> {

                                busy = false;

                                if (error != null) {
                                    message = getErrorMessage(error);
                                } else {

                                    AuthSession.setSession(
                                            result.token(),
                                            result.username()
                                    );

                                    manager.change(
                                            new MenuState(manager)
                                    );
                                }
                            })
                    );
        }
    }

    private String getErrorMessage(Throwable error) {

        Throwable cause = error;

        while (cause instanceof CompletionException
                && cause.getCause() != null) {
            cause = cause.getCause();
        }

        if (cause.getMessage() != null
                && cause.getMessage().contains("ConnectException")) {
            return "Cannot connect to server!";
        }

        return "Request failed. Check your account and try again.";
    }
}
