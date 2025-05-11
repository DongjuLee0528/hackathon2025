package com.example.hackathonback.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // [한 줄 요약] 사용자 점수 및 문제 해결 정보를 저장하는 JPA 엔티티
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // [한 줄 요약] 객체 생성 시 빌더 패턴 사용 가능
public class UserScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // [한 줄 요약] 기본 키 자동 증가
    private Long id;

    // [한 줄 요약] 사용자 엔티티와 N:1 관계 (여러 점수 기록이 하나의 사용자에 연결됨)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 컬럼명: user_id
    private User user;

    private int totalScore;    // [한 줄 요약] 사용자의 총 점수
    private int solvedCount;   // [한 줄 요약] 사용자가 해결한 문제 수

    /**
     * [한 줄 요약] 사용자 평균 점수 계산
     *
     * solvedCount가 0이면 0.0 반환, 그렇지 않으면 총점 / 해결 문제 수
     *
     * @return 평균 점수 (double)
     */
    public double getAverageScore() {
        return solvedCount == 0 ? 0.0 : (double) totalScore / solvedCount;
    }

    /**
     * [한 줄 요약] 평균 점수를 기반으로 사용자 등급(Rank) 반환
     *
     * 기준:
     * - 90 이상: Diamond
     * - 75 이상: Platinum
     * - 60 이상: Gold
     * - 45 이상: Silver
     * - 30 이상: Bronze
     * - 그 외: Iron
     *
     * @return 사용자 등급 문자열
     */
    public String getRank() {
        double average = getAverageScore();
        if (average >= 90) return "Diamond";
        else if (average >= 75) return "Platinum";
        else if (average >= 60) return "Gold";
        else if (average >= 45) return "Silver";
        else if (average >= 30) return "Bronze";
        else return "Iron";
    }
}
