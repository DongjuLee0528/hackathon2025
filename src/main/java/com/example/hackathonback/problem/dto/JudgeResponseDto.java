package com.example.hackathonback.problem.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 채점 결과 응답 DTO
 */
@Getter
@Setter
public class JudgeResponseDto {

    private int score;             // 0~100점 사이의 점수 (채점 기준 기반)
    private String feedback;       // 코드에 대한 피드백 (문제점, 개선 방향 등)
    private String suggestedCode;  // 개선된 코드 예시 (GPT 등에서 제공)

    // Lombok이 자동으로 getter/setter 생성
}
