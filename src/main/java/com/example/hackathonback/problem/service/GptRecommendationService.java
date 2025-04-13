
package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.client.GptApiClient;
import com.example.hackathonback.problem.dto.GptRecommendationRequestDto;
import com.example.hackathonback.problem.dto.RecommendedProblemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GptRecommendationService {

    private final GptApiClient gptApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<RecommendedProblemDto> getRecommendedProblems(GptRecommendationRequestDto dto) {
        String prompt = buildPrompt(dto);
        String response = gptApiClient.getGptResponse(prompt);
        try {
            return Arrays.asList(objectMapper.readValue(response, RecommendedProblemDto[].class));
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패: " + response, e);
        }
    }

    private String buildPrompt(GptRecommendationRequestDto dto) {
        return String.format("""
            사용자 점수: %d
            자주 푼 태그: %s

            위 사용자에게 어울리는 코딩 문제 3개를 추천해줘.
            각 문제는 다음 형식으로 JSON 배열로 출력해:
            [
              { "title": "...", "difficulty": "...", "tags": [...] },
              ...
            ]
        """, dto.getUserScore(), dto.getRecentTags().toString());
    }
}
