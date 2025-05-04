package com.example.hackathonback.user.service;

import com.example.hackathonback.user.entity.UserScore;
import com.example.hackathonback.user.entity.ScoreHistory;
import com.example.hackathonback.user.repository.UserScoreRepository;
import com.example.hackathonback.user.repository.ScoreHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 사용자 점수 및 등급을 관리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class UserScoreService {

    private final ScoreHistoryRepository scoreHistoryRepository; // 점수 이력 저장소
    private final UserScoreRepository scoreRepository;           // 누적 점수 저장소

    /**
     * 사용자 점수를 업데이트하고 점수 이력을 저장
     *
     * @param userId 사용자 ID
     * @param score  새로 획득한 점수
     */
    public void updateScore(Long userId, int score) {
        // 기존 점수 조회 또는 새로 생성
        UserScore userScore = scoreRepository.findByUserId(userId)
                .orElseGet(() -> UserScore.builder()
                        .userId(userId)
                        .totalScore(0)
                        .solvedCount(0)
                        .build());

        // 누적 점수 및 풀이 수 업데이트
        int newTotalScore = userScore.getTotalScore() + score;
        int newSolvedCount = userScore.getSolvedCount() + 1;

        userScore.setTotalScore(newTotalScore);
        userScore.setSolvedCount(newSolvedCount);

        // 저장
        scoreRepository.save(userScore);

        // 점수 변화 이력 저장
        ScoreHistory history = new ScoreHistory();
        history.setUserId(userId);
        history.setScore(newTotalScore);
        history.setSolvedCount(newSolvedCount);
        history.setChangedAt(LocalDateTime.now());

        scoreHistoryRepository.save(history);
    }

    /**
     * 사용자의 현재 등급(랭크) 조회
     *
     * @param userId 사용자 ID
     * @return 등급 문자열 (예: Bronze, Gold 등)
     */
    public String getUserRank(Long userId) {
        return scoreRepository.findByUserId(userId)
                .map(UserScore::getRank)
                .orElse("Unranked");
    }

    /**
     * 사용자의 평균 점수 조회
     *
     * @param userId 사용자 ID
     * @return 평균 점수 (소수점 포함)
     */
    public double getUserAverageScore(Long userId) {
        return scoreRepository.findByUserId(userId)
                .map(UserScore::getAverageScore)
                .orElse(0.0);
    }
}
