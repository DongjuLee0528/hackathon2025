package com.example.hackathonback.user.service;

import com.example.hackathonback.user.entity.ScoreHistory;
import com.example.hackathonback.user.entity.User;
import com.example.hackathonback.user.entity.UserScore;
import com.example.hackathonback.user.repository.ScoreHistoryRepository;
import com.example.hackathonback.user.repository.UserRepository;
import com.example.hackathonback.user.repository.UserScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserScoreService {

    private final ScoreHistoryRepository scoreHistoryRepository;
    private final UserScoreRepository scoreRepository;
    private final UserRepository userRepository;

    public void updateScore(Long userId, int score) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        UserScore userScore = scoreRepository.findByUserId(userId)
                .orElseGet(() -> UserScore.builder()
                        .user(user)
                        .totalScore(0)
                        .solvedCount(0)
                        .build());

        int newTotalScore = userScore.getTotalScore() + score;
        int newSolvedCount = userScore.getSolvedCount() + 1;

        userScore.setTotalScore(newTotalScore);
        userScore.setSolvedCount(newSolvedCount);

        scoreRepository.save(userScore);

        ScoreHistory history = new ScoreHistory();
        history.setUserId(userId);
        history.setScore(newTotalScore);
        history.setSolvedCount(newSolvedCount);
        history.setChangedAt(LocalDateTime.now());

        scoreHistoryRepository.save(history);
    }

    // ✅ 추가된 메서드
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
