package com.example.hackathonback.problem.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String difficulty;
    private String tags;
    private Long templateId;
}
