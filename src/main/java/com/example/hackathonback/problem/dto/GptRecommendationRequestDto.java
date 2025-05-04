package com.example.hackathonback.problem.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * GPT 기반 문제 추천 요청 시 사용되는 DTO
 */
@Getter
@Builder // 빌더 패턴으로 객체 생성 가능
public class GptRecommendationRequestDto {

    private int userScore;             // 사용자의 현재 점수 또는 등급 (ex: 0~500)
    private List<String> recentTags;   // 사용자가 최근에 푼 문제의 태그 목록
}
