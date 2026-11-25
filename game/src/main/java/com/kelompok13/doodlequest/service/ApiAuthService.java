
package com.kelompok13.doodlequest.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ApiAuthService {

    private static final String BASE_URL =
            "http://localhost:8080/api/auth";

    private final HttpClient client =
            HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

    private final ObjectMapper mapper = new ObjectMapper();

    // REGISTER
    public CompletableFuture<String> register(
            String username, String password) {

        return sendRequest("/register", username, password)
                .thenApply(response -> {
                    if (response.statusCode() == 201) {
                        return "Registrasi berhasil!";
                    }

                    throw new RuntimeException(
                            "Registrasi gagal: " + response.body()
                    );
                });
    }

    // LOGIN
    public CompletableFuture<LoginResult> login(
            String username, String password) {

        return sendRequest("/login", username, password)
                .thenApply(response -> {

                    if (response.statusCode() != 200) {
                        throw new RuntimeException(
                                "Login gagal: " + response.body()
                        );
                    }

                    try {
                        return mapper.readValue(
                                response.body(),
                                LoginResult.class
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(
                                "Gagal membaca respons login", e
                        );
                    }
                });
    }

    // LOGOUT
    public CompletableFuture<Void> logout(String token) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/logout"))
                .header("Authorization", "Bearer " + token)
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return client.sendAsync(
                request,
                HttpResponse.BodyHandlers.ofString()
        ).thenApply(response -> {

            if (response.statusCode() != 204) {
                throw new RuntimeException(
                        "Logout gagal: " + response.body()
                );
            }

            return null;
        });
    }

    // MENGIRIM REQUEST KE BACKEND
    private CompletableFuture<HttpResponse<String>> sendRequest(
            String endpoint,
            String username,
            String password) {

        try {
            String json = mapper.writeValueAsString(
                    Map.of(
                            "username", username,
                            "password", password
                    )
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    // FORMAT RESPONSE LOGIN
    public record LoginResult(
            String token,
            String username
    ) {}
}
