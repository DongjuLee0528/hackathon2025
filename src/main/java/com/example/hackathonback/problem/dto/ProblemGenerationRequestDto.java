package com.example.hackathonback.problem.dto;

import com.example.hackathonback.problem.entity.ProblemTag;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 문제 자동 생성 요청 시 사용되는 DTO
 */
@Getter
@Setter
public class ProblemGenerationRequestDto {

    @NotNull(message = "난이도를 입력해주세요.") // null일 경우 유효성 검사 실패 메시지
    private String difficulty;     // 문제 난이도 (예: "초급", "중급", "고급")

    @NotNull(message = "문제 주제를 선택해주세요.") // null일 경우 유효성 검사 실패 메시지
    private ProblemTag tag;        // 문제 주제 (열거형: 배열, 정렬, DFS 등)
}
