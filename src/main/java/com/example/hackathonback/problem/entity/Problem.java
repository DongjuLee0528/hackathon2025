package com.example.hackathonback.problem.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * [한 줄 요약] 문제 정보를 저장하는 JPA 엔티티 클래스
 *
 * 이 클래스는 생성된 코딩 문제의 제목, 설명, 입출력 형식, 예제, 난이도, 태그 등을 DB에 저장하는 역할을 합니다.
 */
@Entity // JPA에서 DB 테이블과 매핑되는 클래스임을 명시
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // [한 줄 요약] 빌더 패턴으로 객체 생성 가능 (생성자와 함께 사용)
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // [한 줄 요약] 기본 키 자동 증가 설정
    private Long id;

    private String title;          // [한 줄 요약] 문제 제목
    private String description;    // [한 줄 요약] 문제 설명
    private String inputFormat;    // [한 줄 요약] 입력 형식 설명
    private String outputFormat;   // [한 줄 요약] 출력 형식 설명
    private String exampleInput;   // [한 줄 요약] 예제 입력
    private String exampleOutput;  // [한 줄 요약] 예제 출력

    @Enumerated(EnumType.STRING) // [한 줄 요약] enum 이름 그대로 문자열로 DB에 저장
    private Difficulty difficulty;  // [한 줄 요약] 문제 난이도 (EASY, MEDIUM, HARD)

    @Enumerated(EnumType.STRING)
    private ProblemTag tag;         // [한 줄 요약] 문제 주제 태그 (예: 문자열, 정렬 등)

    private Long templateId;        // [한 줄 요약] 문제와 연결된 코드 템플릿 ID (AI 생성 템플릿 등)
}
