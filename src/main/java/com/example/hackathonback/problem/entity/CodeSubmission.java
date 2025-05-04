package com.example.hackathonback.problem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * 사용자의 코드 제출 이력을 저장하는 엔티티
 */
@Entity
public class CodeSubmission {

    @Id
    @GeneratedValue // 기본 키 자동 생성 (auto-increment)
    private Long id;

    private String userId;     // 제출한 사용자 ID
    private String problemId;  // 해당 문제 ID (문제 식별용)
    private String code;       // 제출한 코드 내용
    private String result;     // GPT 또는 채점 결과 (전체 응답 저장 가능)

    // 생성자, getter/setter 필요 시 Lombok(@Getter, @Setter, @NoArgsConstructor 등) 사용 가능
}
