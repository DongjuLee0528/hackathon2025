package com.example.hackathonback.problem.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * GPT가 추천한 문제 정보를 담는 DTO
 */
@Getter
@Setter
public class RecommendedProblemDto {

    private String title;             // 문제 제목
    private String difficulty;        // 난이도 (예: 초급, 중급, 고급)
    private List<String> tags;        // 문제 관련 태그 목록 (예: 배열, 정렬, DFS 등)
}
