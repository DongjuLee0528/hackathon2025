package com.example.hackathonback.problem.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 문제 정보를 저장하는 JPA 엔티티 클래스
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 증가
    private Long id;

    private String title;           // 문제 제목
    private String description;     // 문제 설명 (전체 내용)
    private String inputFormat;     // 입력 형식 설명
    private String outputFormat;    // 출력 형식 설명
    private String exampleInput;    // 예제 입력
    private String exampleOutput;   // 예제 출력

    private String difficulty;      // 문제 난이도 (예: 초급, 중급, 고급)
    private String tags;            // 태그 문자열 (쉼표로 구분된 여러 개 가능)

    private Long templateId;        // 문제 템플릿 ID (예: 언어별 템플릿 연결용)
}
