package com.kelompok13.doodlequest.backend.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Body untuk register dan login. */
public record AuthRequest(
        @NotBlank(message = "Username wajib diisi")
        @Size(min = 3, max = 20, message = "Username 3-20 karakter")
        @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "Username hanya huruf, angka, dan underscore")
        String username,

        @NotBlank(message = "Password wajib diisi")
        @Size(min = 6, max = 72, message = "Password 6-72 karakter")
        String password) {
}
