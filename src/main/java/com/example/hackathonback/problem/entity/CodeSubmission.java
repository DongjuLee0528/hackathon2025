package com.example.hackathonback.problem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class CodeSubmission {

    @Id
    @GeneratedValue
    private Long id;

    private String userId;
    private String problemId;
    private String code;
    private String result; // GPT 응답 전체 저장 (선택)

    // 생성자, getter/setter
}

