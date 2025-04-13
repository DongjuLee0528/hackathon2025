
package com.example.hackathonback.problem.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GptRecommendationRequestDto {
    private int userScore;
    private List<String> recentTags;
}
