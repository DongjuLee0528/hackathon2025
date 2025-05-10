package com.example.hackathonback.problem.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문제 난이도")
public enum Difficulty {
    @Schema(description = "쉬움")
    EASY,

    @Schema(description = "중간")
    MEDIUM,

    @Schema(description = "어려움")
    HARD
}
