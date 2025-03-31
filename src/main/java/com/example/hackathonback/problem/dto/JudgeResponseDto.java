package com.example.hackathonback.problem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JudgeResponseDto {
    private int score;             // 0~100점 사이의 점수
    private String feedback;       // 개선이 필요한 피드백
    private String suggestedCode;  // 수정된 코드 예시

    // getter/setter
}
