package com.example.hackathonback.problem.dto;

import lombok.Data;

@Data
public class ProblemResponseDto {
    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String exampleInput;
    private String exampleOutput;
}
