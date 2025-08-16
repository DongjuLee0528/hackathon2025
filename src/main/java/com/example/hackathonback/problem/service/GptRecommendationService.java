package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.client.GptApiClient;
import com.example.hackathonback.problem.dto.GptRecommendationRequestDto;
import com.example.hackathonback.problem.dto.RecommendedProblemDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**

 GPT를 활용해 사용자의 실력/태그 기반으로 문제를 추천하는 서비스
 */
@Service
@RequiredArgsConstructor
public class GptRecommendationService {

    private final GptApiClient gptApiClient; // GPT API 호출
    private final ObjectMapper objectMapper; // Spring 기본 ObjectMapper 빈

    /**

     사용자 정보 기반 문제 추천 요청 → GPT 응답(JSON) → DTO 리스트로 변환
     */
    public List<RecommendedProblemDto> getRecommendedProblems(GptRecommendationRequestDto dto) {
        validate(dto);

        String prompt = buildPrompt(dto);
        String response = gptApiClient.getGptResponse(prompt);

        try {
            String jsonArray = extractJsonArray(response); // 설명/코드펜스 제거 후 배열만 파싱
            return objectMapper.readValue(jsonArray, new TypeReference<List<RecommendedProblemDto>>() {});
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패: " + safeTruncate(response, 400), e);
        }
    }

    /** 필수 입력 검증 */
    private void validate(GptRecommendationRequestDto dto) {
        if (dto == null) throw new IllegalArgumentException("요청이 비어 있습니다.");
        if (!StringUtils.hasText(dto.getLanguage())) throw new IllegalArgumentException("language는 필수입니다.");
        if (!StringUtils.hasText(dto.getProblemType())) throw new IllegalArgumentException("problemType은 필수입니다.");
        if (!StringUtils.hasText(dto.getDifficulty())) throw new IllegalArgumentException("difficulty는 필수입니다.");
    }

    /**

     GPT에 전달할 프롬프트 생성

     반드시 JSON 배열만 출력하도록 지시
     */
    private String buildPrompt(GptRecommendationRequestDto dto) {
        return """
아래 사용자 정보를 바탕으로 코딩 문제 3개를 추천하세요.
반드시 'JSON 배열'만 출력하고, 설명 문구나 코드펜스(``` 등)는 절대 포함하지 마세요.

   사용자 점수: %d
   자주 푼 태그: %s
   선택한 언어: %s
   문제 유형: %s
   난이도: %s

   출력 형식 예시:
   [
     { "title": "문제 제목", "difficulty": "easy|medium|hard", "tags": ["배열","정렬"] },
     { "title": "...", "difficulty": "...", "tags": ["..."] },
     { "title": "...", "difficulty": "...", "tags": ["..."] }
   ]
   """.formatted(
                dto.getUserScore(),
                String.valueOf(dto.getRecentTags()),
                dto.getLanguage(),
                dto.getProblemType(),
                dto.getDifficulty()


        );
    }

    /**

     GPT 응답에서 JSON 배열만 추출

     응답에 설명/코드펜스가 섞여 있어도 첫 '[' ~ 마지막 ']' 구간만 반환
     */
    private String extractJsonArray(String s) {
        if (!StringUtils.hasText(s)) throw new IllegalStateException("빈 응답입니다.");
        String src = s.trim();

// 코드펜스 제거
        if (src.startsWith("")) { int first = src.indexOf('\n'); int lastFence = src.lastIndexOf("");
            if (first > 0 && lastFence > first) {
                src = src.substring(first + 1, lastFence).trim();
            }
        }

        int start = src.indexOf('[');
        int end = src.lastIndexOf(']');
        if (start == -1 || end == -1 || end <= start) {
            throw new IllegalStateException("JSON 배열을 찾지 못했습니다.");
        }
        return src.substring(start, end + 1).trim();
    }

    private String safeTruncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }
}