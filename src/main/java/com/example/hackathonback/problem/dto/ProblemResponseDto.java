package com.example.hackathonback.problem.dto;

import lombok.Data;

/**
 * 문제 생성 또는 조회 시 응답으로 전달되는 DTO
 */
@Data // Getter, Setter, toString, equals, hashCode 등을 자동 생성
public class ProblemResponseDto {

    private String title;          // 문제 제목
    private String description;    // 문제 설명 (전체 문제 내용)
    private String inputFormat;    // 입력 형식 설명
    private String outputFormat;   // 출력 형식 설명
    private String exampleInput;   // 예제 입력
    private String exampleOutput;  // 예제 출력
}
