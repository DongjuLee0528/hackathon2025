package com.example.hackathonback.problem.logic;

import com.example.hackathonback.problem.dto.JudgeResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * GPT API를 이용하여 사용자 코드를 자동 채점하는 로직 클래스
 */
@Component
public class GptJudgeLogic {

    /** OpenAI API 키 (env 또는 application.yml) */
    private final String apiKey;

    /** 사용할 모델명 */
    private final String model;

    /** OpenAI Base URL */
    private final String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public GptJudgeLogic(
            RestTemplateBuilder builder,
            @Value("${gpt.judge.api.key:${GPT_JUDGE_KEY:}}") String apiKey,
            @Value("${gpt.judge.model:${GPT_JUDGE_MODEL:gpt-4o-mini}}") String model,
            @Value("${gpt.base-url:https://api.openai.com}") String baseUrl
    ) {
        this.apiKey = apiKey;
        this.model = model;
        this.baseUrl = trimTrailingSlash(baseUrl);
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(20))
                .build();
    }

    /**
     * GPT에게 문제와 사용자 코드를 기반으로 채점 요청을 보냄
     *
     * @param problem 문제 설명 (텍스트)
     * @param code 사용자 제출 코드
     * @param lang 사용 언어 (예: java, python)
     * @return GPT가 반환한 채점 결과 DTO
     */
    public JudgeResponseDto sendJudgeRequest(String problem, String code, String lang) {
        // 기본 검증
        if (isBlank(problem) || isBlank(code) || isBlank(lang)) {
            throw new IllegalArgumentException("problem/code/lang는 비어 있을 수 없습니다.");
        }
        if (isBlank(apiKey)) {
            throw new IllegalStateException("OpenAI API 키가 설정되지 않았습니다. (gpt.judge.api.key / GPT_JUDGE_KEY)");
        }
        if (isBlank(model)) {
            throw new IllegalStateException("모델명이 설정되지 않았습니다. (gpt.judge.model / GPT_JUDGE_MODEL)");
        }

        String prompt = makePrompt(problem, code, lang); // GPT 프롬프트 생성

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // 요청 바디 (Chat Completions)
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.2 // 채점은 일관성이 중요 → 낮은 temperature 권장
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    baseUrl + "/v1/chat/completions",
                    request,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalStateException("OpenAI 응답이 비정상입니다: " + response.getStatusCode());
            }

            // 응답 JSON에서 choices[0].message.content 추출
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode choices = nonNull(root.path("choices"), "choices가 없습니다.");
            if (!choices.isArray() || choices.isEmpty()) {
                throw new IllegalStateException("OpenAI 응답에 유효한 choices가 없습니다.");
            }
            JsonNode message = nonNull(choices.get(0).path("message"), "message가 없습니다.");
            JsonNode contentNode = nonNull(message.path("content"), "content가 없습니다.");

            // content는 일반적으로 "문자열(JSON 텍스트)" → 문자열을 다시 JSON으로 파싱
            String contentText = contentNode.asText(); // ← *** 기존 코드의 핵심 버그 수정: content.toString() 사용 금지 ***
            JsonNode judged = mapper.readTree(contentText);

            JudgeResponseDto result = mapper.treeToValue(judged, JudgeResponseDto.class);

            // 점수 범위 보정(0~100)
            if (result.getScore() < 0) result.setScore(0);
            if (result.getScore() > 100) result.setScore(100);

            // 추천 코드 기본값
            if (isBlank(result.getSuggestedCode())) {
                result.setSuggestedCode("// 추천 코드 없음");
            }

            // 피드백 기본값
            if (isBlank(result.getFeedback())) {
                result.setFeedback("피드백이 비어 있습니다.");
            }

            return result;

        } catch (RestClientResponseException e) {
            String bodyText = e.getResponseBodyAsString();
            throw new IllegalStateException(
                    "OpenAI API 호출 실패 (" + e.getRawStatusCode() + "): " + (bodyText == null ? "" : truncate(bodyText, 800)),
                    e
            );
        } catch (ResourceAccessException e) {
            throw new IllegalStateException("OpenAI API 네트워크 오류/타임아웃", e);
        } catch (Exception e) {
            throw new IllegalStateException("OpenAI API 호출/파싱 중 오류", e);
        }
    }

    /**
     * GPT에게 전달할 프롬프트 문자열 생성
     * - 모델이 JSON으로 정확히 응답하도록 지시 포함
     */
    private String makePrompt(String problem, String code, String lang) {
        return """
다음은 문제와 사용자 코드입니다.

문제 설명:
%s

사용자 코드 (%s):
%s

다음 JSON 형식으로만(그 외 텍스트/마크다운/설명 금지) 정확히 응답하세요:
{
  "score": 0~100 (정수),
  "feedback": "피드백 내용(간결하게)",
  "suggestedCode": "개선된 코드 예시(없으면 빈 문자열)"
}
""".formatted(problem, lang, code);
    }

    /* ---------- 내부 유틸 ---------- */

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String trimTrailingSlash(String base) {
        if (base == null) return "";
        return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }

    private JsonNode nonNull(JsonNode node, String msgIfMissing) {
        if (node == null || node.isMissingNode() || node.isNull()) {
            throw new IllegalStateException(msgIfMissing);
        }
        return node;
    }
}
