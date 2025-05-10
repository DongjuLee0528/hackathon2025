package com.example.hackathonback.problem.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 문제 정보를 저장하는 JPA 엔티티 클래스
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String exampleInput;
    private String exampleOutput;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;  // 난이도 enum

    @Enumerated(EnumType.STRING)
    private ProblemTag tag;         // 태그 enum

    private Long templateId;
}
