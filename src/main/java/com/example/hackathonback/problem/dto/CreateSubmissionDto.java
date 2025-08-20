package com.example.hackathonback.problem.dto;

public record CreateSubmissionDto(
        String userId,
        String problemId,
        String code,
        String resultJson
) {}
