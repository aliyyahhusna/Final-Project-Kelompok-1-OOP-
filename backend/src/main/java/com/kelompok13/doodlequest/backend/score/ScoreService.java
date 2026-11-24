
package com.kelompok13.doodlequest.backend.score;

import com.kelompok13.doodlequest.backend.user.AuthService;
import com.kelompok13.doodlequest.backend.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {

    private final ScoreRepository scores;
    private final AuthService authService;

    public ScoreService(ScoreRepository scores, AuthService authService) {
        this.scores = scores;
        this.authService = authService;
    }

    // Menyimpan skor pemain yang sedang login
    public ScoreResponse saveScore(String authorization, ScoreRequest request) {

        User user = authService.requireUser(authorization);

        Score score = new Score(request.score(), user);

        Score savedScore = scores.save(score);

        return toResponse(savedScore);
    }

    // Mengambil seluruh riwayat skor milik pemain
    public List<ScoreResponse> getMyScores(String authorization) {

        User user = authService.requireUser(authorization);

        return scores.findAllByUser_IdOrderByScoreDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Mengambil skor tertinggi milik pemain
    public int getMyHighScore(String authorization) {

        User user = authService.requireUser(authorization);

        return scores.findAllByUser_IdOrderByScoreDesc(user.getId())
                .stream()
                .mapToInt(Score::getScore)
                .max()
                .orElse(0);
    }

    // Mengambil 10 skor tertinggi seluruh pemain
    public List<ScoreResponse> getLeaderboard() {

        return scores.findTop10ByOrderByScoreDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Mengubah data Score menjadi ScoreResponse
    private ScoreResponse toResponse(Score score) {

        return new ScoreResponse(
                score.getId(),
                score.getUser().getUsername(),
                score.getScore(),
                score.getCreatedAt()
        );
    }
}
