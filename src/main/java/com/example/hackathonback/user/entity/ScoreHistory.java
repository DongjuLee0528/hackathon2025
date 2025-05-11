package com.example.hackathonback.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * [한 줄 요약] 사용자 점수 변동 이력을 저장하는 JPA 엔티티
 *
 * 이 클래스는 사용자의 점수 변화 및 누적 문제 해결 수를 기록하여
 * 점수 기록 히스토리를 DB에 저장하는 역할을 합니다.
 */
@Entity
public class ScoreHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // [한 줄 요약] 자동 증가되는 기본 키
    private Long id;

    private Long userId;         // [한 줄 요약] 점수가 변경된 사용자 ID

    private Integer score;       // [한 줄 요약] 해당 시점의 점수
    private Integer solvedCount; // [한 줄 요약] 해당 시점의 누적 해결 문제 수
    private LocalDateTime changedAt; // [한 줄 요약] 점수가 변경된 시간

    // --- Getter / Setter 메서드 ---

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
