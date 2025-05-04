package com.example.hackathonback.problem.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 채점 요청 시 사용되는 DTO
 */
@Getter
@Setter
public class JudgeRequestDto {

    private String problemDescription; // 문제 설명 (입력, 출력 형식 포함 전체 설명)
    private String userCode;           // 사용자 제출 코드
    private String language;           // 사용 언어 (예: "java", "python")

    // Lombok이 자동으로 getter/setter 생성
}
