package com.example.hackathonback.problem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class CodeSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String problemId;

    @Lob
    private String code;       // (수동 저장 시 사용)

    @Lob
    private String savedCode;  // judge/solve + saveRaw=true 일 때만 저장

    private String codeHash;   // 원문 비저장 시 중복/추적용

    @Lob
    private String result;     // JudgeResponseDto JSON or 응답 원문

    private LocalDateTime createdAt = LocalDateTime.now();
}
