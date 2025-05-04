package com.example.hackathonback.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 사용자 점수 변동 이력을 저장하는 엔티티
 */
@Entity
public class ScoreHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성
    private Long id;

    private Long userId;         // 사용자 ID (필수 필드)

    private Integer score;       // 변경된 점수
    private Integer solvedCount; // 누적 문제 해결 수
    private LocalDateTime changedAt; // 변경된 시간

    // Getter / Setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(Integer solvedCount) {
        this.solvedCount = solvedCount;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
