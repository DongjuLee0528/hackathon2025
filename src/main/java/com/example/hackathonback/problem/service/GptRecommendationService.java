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
 * [한 줄 요약] GPT를 활용해 사용자의 실력 및 태그 기반으로 문제를 추천하는 서비스
 *
 * 이 서비스는 사용자의 점수, 언어, 태그 등을 기반으로 프롬프트를 생성하여 GPT에게 문제 추천을 요청하고,
 * 그 응답(JSON 문자열)을 파싱하여 클라이언트에게 전달 가능한 형태로 변환합니다.
 */
@Service
@RequiredArgsConstructor // [한 줄 요약] 생성자 자동 주입 (gptApiClient 의존성 주입)
public class GptRecommendationService {

    private final GptApiClient gptApiClient; // [한 줄 요약] GPT API 호출을 담당하는 클라이언트
    private final ObjectMapper objectMapper = new ObjectMapper(); // [한 줄 요약] JSON 파싱 도구 (Jackson)

    /**
     * [한 줄 요약] 사용자 정보 기반 문제 추천 요청 → GPT 응답 → DTO로 파싱
     *
     * @param dto 사용자 점수, 선호 태그, 언어, 문제 유형, 난이도 정보가 담긴 요청 DTO
     * @return GPT로부터 추천받은 문제 리스트 (RecommendedProblemDto 형태)
     */
    public List<RecommendedProblemDto> getRecommendedProblems(GptRecommendationRequestDto dto) {
        String prompt = buildPrompt(dto); // [한 줄 요약] 사용자 정보 기반 프롬프트 생성
        String response = gptApiClient.getGptResponse(prompt); // [한 줄 요약] GPT 호출 후 응답 받기

        try {
            // [한 줄 요약] GPT 응답(JSON 문자열)을 RecommendedProblemDto[]로 변환
            return Arrays.asList(objectMapper.readValue(response, RecommendedProblemDto[].class));
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패: " + response, e);
        }
    }

    /**
     * [한 줄 요약] GPT에게 보낼 프롬프트 문자열 생성
     *
     * 사용자 점수, 태그, 언어, 유형, 난이도를 기반으로 GPT가 이해할 수 있는 문제 추천 요청을 구성합니다.
     *
     * @param dto 사용자 요청 정보
     * @return GPT에 전달할 프롬프트 문자열
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
