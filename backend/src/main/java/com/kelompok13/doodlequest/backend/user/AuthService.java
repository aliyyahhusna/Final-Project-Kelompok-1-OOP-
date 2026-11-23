package com.kelompok13.doodlequest.backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final SecureRandom random = new SecureRandom();

    // token -> id user. Disimpan di memori: restart backend = login ulang.
    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public UserResponse register(AuthRequest request) {
        if (users.existsByUsernameIgnoreCase(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username sudah dipakai");
        }
        User saved = users.save(new User(request.username(), encoder.encode(request.password())));
        return new UserResponse(saved.getId(), saved.getUsername());
    }

    public LoginResponse login(AuthRequest request) {
        // Pesan error sama untuk username salah / password salah (tidak membocorkan akun mana yang ada).
        User user = users.findByUsernameIgnoreCase(request.username())
                .filter(u -> encoder.matches(request.password(), u.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Username atau password salah"));

        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokens.put(token, user.getId());
        return new LoginResponse(token, user.getUsername());
    }

    /**
     * DIPAKAI ALIYYAH (score): mengubah header "Authorization: Bearer <token>"
     * menjadi user yang login. Kosong = token tidak ada / tidak valid -> balas 401.
     */
    public Optional<User> authenticate(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }
        Long userId = tokens.get(authorizationHeader.substring(7).trim());
        return userId == null ? Optional.empty() : users.findById(userId);
    }

    /** Sama seperti authenticate, tapi langsung melempar 401 jika gagal. */
    public User requireUser(String authorizationHeader) {
        return authenticate(authorizationHeader).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Belum login atau token tidak valid"));
    }

    public void logout(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            tokens.remove(authorizationHeader.substring(7).trim());
        }
    }
}
