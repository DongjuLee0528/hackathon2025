package com.example.hackathonback.problem.dto;

public record SolveRequest(
        String problemId,
        String language,
        String sourceCode,
        Boolean saveRaw // 원문 코드 저장 동의(옵트인)
) {}
