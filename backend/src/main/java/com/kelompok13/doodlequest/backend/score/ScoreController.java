
package com.kelompok13.doodlequest.backend.score;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    // Menyimpan skor pemain yang sedang login
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScoreResponse saveScore(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody ScoreRequest request) {

        return scoreService.saveScore(authorization, request);
    }

    // Mengambil seluruh riwayat skor pemain
    @GetMapping("/me")
    public List<ScoreResponse> getMyScores(
            @RequestHeader("Authorization") String authorization) {

        return scoreService.getMyScores(authorization);
    }

    // Mengambil skor tertinggi pemain
    @GetMapping("/high-score")
    public int getMyHighScore(
            @RequestHeader("Authorization") String authorization) {

        return scoreService.getMyHighScore(authorization);
    }

    // Mengambil leaderboard 10 pemain dengan skor tertinggi
    @GetMapping("/leaderboard")
    public List<ScoreResponse> getLeaderboard() {

        return scoreService.getLeaderboard();
    }
}
