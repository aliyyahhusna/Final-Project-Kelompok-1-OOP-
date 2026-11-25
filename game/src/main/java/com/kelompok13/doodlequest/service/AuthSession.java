
package com.kelompok13.doodlequest.service;

public final class AuthSession {

    private static String token;
    private static String username;

    private AuthSession() {
    }

    public static void setSession(
            String newToken,
            String newUsername) {

        token = newToken;
        username = newUsername;
    }

    public static String getToken() {
        return token;
    }

    public static String getUsername() {
        return username;
    }

    public static boolean isLoggedIn() {
        return token != null && !token.isBlank();
    }

    public static void clear() {
        token = null;
        username = null;
    }
}
