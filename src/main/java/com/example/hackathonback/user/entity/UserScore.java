package com.example.hackathonback.user.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자의 누적 점수 및 등급 정보를 관리하는 엔티티
 */
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 증가
    private Long id;

    private Long userId;        // 사용자 ID (User 엔티티 참조용)

    private int totalScore;     // 누적 점수
    private int solvedCount;    // 총 푼 문제 수

    /**
     * 평균 점수 계산 (소수점 포함)
     */
    public double getAverageScore() {
        return solvedCount == 0 ? 0 : (double) totalScore / solvedCount;
    }

    /**
     * 평균 점수에 따른 등급 반환
     */
    public String getRank() {
        double avg = getAverageScore();
        if (avg < 20) return "Iron";
        else if (avg < 40) return "Bronze";
        else if (avg < 60) return "Silver";
        else if (avg < 75) return "Gold";
        else if (avg < 90) return "Platinum";
        else return "Diamond";
    }
}
