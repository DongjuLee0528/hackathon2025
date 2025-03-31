package com.example.hackathonback.problem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JudgeRequestDto {
    private String problemDescription; // 문제 설명
    private String userCode;           // 사용자 제출 코드
    private String language;           // 언어 (예: "python", "java")

    // getter/setter
}