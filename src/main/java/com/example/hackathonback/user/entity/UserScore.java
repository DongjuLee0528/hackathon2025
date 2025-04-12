package com.example.hackathonback.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private int totalScore;     // 누적 점수
    private int solvedCount;    // 푼 문제 수

    public double getAverageScore() {
        return solvedCount == 0 ? 0 : (double) totalScore / solvedCount;
    }

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
