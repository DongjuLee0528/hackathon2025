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

@Service // [한 줄 요약] 사용자 점수 계산 및 히스토리 저장을 담당하는 서비스
@RequiredArgsConstructor
public class UserScoreService {

    private final ScoreHistoryRepository scoreHistoryRepository; // [한 줄 요약] 점수 변동 기록 저장소
    private final UserScoreRepository scoreRepository;           // [한 줄 요약] 사용자 점수 조회/저장소
    private final UserRepository userRepository;                 // [한 줄 요약] 사용자 조회용 레포지토리

    /**
     * [한 줄 요약] 사용자 점수를 갱신하고 점수 변경 이력을 기록
     *
     * 1. 사용자 조회 → 점수 객체(UserScore) 조회 또는 생성
     * 2. 점수/해결 수 업데이트 후 저장
     * 3. ScoreHistory에 변경 이력 기록
     *
     * @param userId 사용자 ID
     * @param score 새로 획득한 점수
     */
    public void updateScore(Long userId, int score) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 기존 점수 기록이 없으면 새로 생성
        UserScore userScore = scoreRepository.findByUserId(userId)
                .orElseGet(() -> UserScore.builder()
                        .user(user)
                        .totalScore(0)
                        .solvedCount(0)
                        .build());

        // 점수 및 문제 해결 수 업데이트
        int newTotalScore = userScore.getTotalScore() + score;
        int newSolvedCount = userScore.getSolvedCount() + 1;

        userScore.setTotalScore(newTotalScore);
        userScore.setSolvedCount(newSolvedCount);

        scoreRepository.save(userScore); // 사용자 점수 저장

        // 점수 변경 이력 저장
        ScoreHistory history = new ScoreHistory();
        history.setUserId(userId);
        history.setScore(newTotalScore);
        history.setSolvedCount(newSolvedCount);
        history.setChangedAt(LocalDateTime.now());

        scoreHistoryRepository.save(history);
    }

    /**
     * [한 줄 요약] 사용자 등급(Rank) 조회
     *
     * UserScore 객체가 존재하면 해당 등급 반환, 없으면 "Unranked" 반환
     *
     * @param userId 사용자 ID
     * @return 사용자 티어 (예: Gold, Silver 등)
     */
    public String getUserRank(Long userId) {
        return scoreRepository.findByUserId(userId)
                .map(UserScore::getRank)
                .orElse("Unranked");
    }

    /**
     * [한 줄 요약] 사용자 평균 점수 조회
     *
     * @param userId 사용자 ID
     * @return 평균 점수 (없으면 0.0)
     */
    public double getUserAverageScore(Long userId) {
        return scoreRepository.findByUserId(userId)
                .map(UserScore::getAverageScore)
                .orElse(0.0);
    }
}
