package com.example.hackathonback.problem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String inputExample;
    private String outputExample;
    private String difficulty;
    private String tags;

    private Long templateId; // 어떤 템플릿에서 만들어졌는지 추적용
}
