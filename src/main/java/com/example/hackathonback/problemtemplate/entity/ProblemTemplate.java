package com.example.hackathonback.problemtemplate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProblemTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String tags; // 예: "배열, 구현, 조건문"
    private String difficulty; // 예: "easy", "medium", "hard"
}
