
package com.kelompok13.doodlequest.backend.score;

import java.time.LocalDateTime;

public record ScoreResponse(
        Long id,
        String username,
        int score,
        LocalDateTime createdAt
) {
}
