package com.example.hackathonback.problem.dto;

import com.example.hackathonback.problem.entity.ProblemTag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemGenerationRequestDto {

    @NotNull(message = "난이도를 입력해주세요.")
    @Schema(description = "문제 난이도 (예: EASY, MEDIUM, HARD)", example = "EASY")
    private String difficulty;

    @NotNull(message = "문제 주제를 선택해주세요.")
    @Schema(description = "문제 태그", implementation = ProblemTag.class)
    private ProblemTag tag;

    @Schema(description = "사용 언어", example = "JAVA")
    private String language;

    @Schema(description = "문제 유형", example = "OUTPUT")
    private String problemType;
}
