package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.client.GptApiClient;
import com.example.hackathonback.problem.dto.GptRecommendationRequestDto;
import com.example.hackathonback.problem.dto.RecommendedProblemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * GPT를 활용해 사용자의 실력 및 관심 태그 기반으로 문제를 추천하는 서비스
 */
@Service
@RequiredArgsConstructor
public class GptRecommendationService {

    private final GptApiClient gptApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱용

    /**
     * 사용자 점수 및 태그를 기반으로 GPT에게 문제 추천 요청을 보내고 결과를 파싱함
     *
     * @param dto 사용자 정보(점수, 최근 태그 목록, 선택 정보 포함)
     * @return 추천 문제 목록
     */
    public List<RecommendedProblemDto> getRecommendedProblems(GptRecommendationRequestDto dto) {
        String prompt = buildPrompt(dto); // 프롬프트 구성
        String response = gptApiClient.getGptResponse(prompt); // GPT API 호출

        try {
            // JSON 배열 형태의 문자열 응답을 RecommendedProblemDto[]로 역직렬화
            return Arrays.asList(objectMapper.readValue(response, RecommendedProblemDto[].class));
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패: " + response, e);
        }
    }

    /**
     * GPT에게 보낼 추천 요청용 프롬프트 생성
     */
    private String buildPrompt(GptRecommendationRequestDto dto) {
        return String.format("""
            사용자 점수: %d
            자주 푼 태그: %s
            선택한 언어: %s
            문제 유형: %s
            난이도: %s

            위 조건에 맞는 코딩 문제 3개를 추천해줘.
            각 문제는 다음 형식으로 JSON 배열로 출력해:
            [
              { "title": "...", "difficulty": "...", "tags": [...] },
              ...
            ]
        """,
            dto.getUserScore(),
            dto.getRecentTags().toString(),
            dto.getLanguage(),
            dto.getProblemType(),
            dto.getDifficulty()
        );
    }
}
