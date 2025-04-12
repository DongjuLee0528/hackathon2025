package com.example.hackathonback.user.service;

import com.example.hackathonback.user.entity.UserScore;
import com.example.hackathonback.user.entity.ScoreHistory;
import com.example.hackathonback.user.repository.UserScoreRepository;
import com.example.hackathonback.user.repository.ScoreHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserScoreService {

    private final ScoreHistoryRepository scoreHistoryRepository;
    private final UserScoreRepository scoreRepository;

    public void updateScore(Long userId, int score) {
        UserScore userScore = scoreRepository.findByUserId(userId)
                .orElseGet(() -> UserScore.builder()
                        .userId(userId)
                        .totalScore(0)
                        .solvedCount(0)
                        .build());

        // 기존 점수 값 저장
        int newTotalScore = userScore.getTotalScore() + score;
        int newSolvedCount = userScore.getSolvedCount() + 1;

        userScore.setTotalScore(newTotalScore);
        userScore.setSolvedCount(newSolvedCount);

        scoreRepository.save(userScore);

        // 🔥 점수 변화 이력 저장
        ScoreHistory history = new ScoreHistory();
        history.setUserId(userId);
        history.setScore(newTotalScore);
        history.setSolvedCount(newSolvedCount);
        history.setChangedAt(LocalDateTime.now());

        scoreHistoryRepository.save(history);
    }

    public String getUserRank(Long userId) {
        return scoreRepository.findByUserId(userId)
                .map(UserScore::getRank)
                .orElse("Unranked");
    }

    public double getUserAverageScore(Long userId) {
        return scoreRepository.findByUserId(userId)
                .map(UserScore::getAverageScore)
                .orElse(0.0);
    }
}
