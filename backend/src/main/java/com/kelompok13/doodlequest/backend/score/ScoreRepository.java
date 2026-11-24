
package com.kelompok13.doodlequest.backend.score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findAllByUser_IdOrderByScoreDesc(Long userId);

    List<Score> findTop10ByOrderByScoreDesc();

    List<Score> findAllByOrderByScoreDesc();
}
