package com.example.hackathonback.problem.dto;

import com.example.hackathonback.problem.entity.ProblemTag;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 문제 자동 생성 요청 시 사용되는 DTO
 * 기존: 난이도 + 주제 기반
 * 확장: 언어 + 문제 유형 기반 GPT 문제 생성을 위한 필드 추가
 */
@Getter
@Setter
public class ProblemGenerationRequestDto {

    @NotNull(message = "난이도를 입력해주세요.")
    private String difficulty;

    @NotNull(message = "문제 주제를 선택해주세요.")
    private ProblemTag tag;

    // GPT 문제 생성을 위한 필드 (프론트에서 선택할 경우 사용)
    private String language;
    private String problemType;
}