package com.example.hackathonback.problem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemGenerationRequestDto {
    private String difficulty;
    private String tag;
}
