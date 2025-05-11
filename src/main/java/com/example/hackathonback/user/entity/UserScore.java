package com.example.hackathonback.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 외래 키 매핑: user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int totalScore;
    private int solvedCount;

    public double getAverageScore() {
        return solvedCount == 0 ? 0.0 : (double) totalScore / solvedCount;
    }

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
