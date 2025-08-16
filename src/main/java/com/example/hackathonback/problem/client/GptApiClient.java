package com.example.hackathonback.problem.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * OpenAI GPT API 클라이언트
 * - Chat Completions 엔드포인트 호출
 * - 타임아웃/에러 핸들링 강화
 * - 환경변수/설정(@Value)로 키/모델/베이스URL 주입
 */
@Component
public class GptApiClient {

    private final RestTemplate restTemplate;
    private final String apiKey;   // gpt.problem.key 또는 환경변수 GPT_PROBLEM_KEY
    private final String model;    // gpt.problem.model 또는 환경변수 GPT_PROBLEM_MODEL
    private final String baseUrl;  // gpt.base-url (기본 https://api.openai.com)

    public GptApiClient(
            RestTemplateBuilder builder,
            @Value("${gpt.problem.key:${GPT_PROBLEM_KEY:}}") String apiKey,
            @Value("${gpt.problem.model:${GPT_PROBLEM_MODEL:gpt-4o-mini}}") String model,
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
     * 프롬프트를 보내고 응답 텍스트를 반환
     */
    public String getGptResponse(String prompt) {
        if (isBlank(prompt)) {
            throw new IllegalArgumentException("prompt는 비어 있을 수 없습니다.");
        }
        if (isBlank(apiKey)) {
            throw new IllegalStateException("OpenAI API 키가 설정되지 않았습니다. (gpt.problem.key / GPT_PROBLEM_KEY)");
        }
        if (isBlank(model)) {
            throw new IllegalStateException("모델명이 설정되지 않았습니다. (gpt.problem.model / GPT_PROBLEM_MODEL)");
        }

        // 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.USER_AGENT, "hackathonBack/1.0");

        // 바디 (Chat Completions)
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    baseUrl + "/v1/chat/completions",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalStateException("OpenAI 응답이 비정상입니다: " + response.getStatusCode());
            }

            // choices[0].message.content 추출
            Object choicesObj = response.getBody().get("choices");
            if (!(choicesObj instanceof List<?> choices) || choices.isEmpty()) {
                throw new IllegalStateException("OpenAI 응답에 choices가 없습니다.");
            }
            Object first = choices.get(0);
            if (!(first instanceof Map<?, ?> firstMap)) {
                throw new IllegalStateException("OpenAI 응답 형식이 올바르지 않습니다.(choice)");
            }
            Object messageObj = firstMap.get("message");
            if (!(messageObj instanceof Map<?, ?> msgMap)) {
                throw new IllegalStateException("OpenAI 응답 형식이 올바르지 않습니다.(message)");
            }
            Object contentObj = msgMap.get("content");
            String content = contentObj == null ? "" : contentObj.toString();

            if (content.isBlank()) {
                throw new IllegalStateException("OpenAI 응답 content가 비어 있습니다.");
            }
            return content;

        } catch (RestClientResponseException e) {
            // 4xx/5xx 응답(본문 포함)
            String bodyText = e.getResponseBodyAsString();
            throw new IllegalStateException(
                    "OpenAI API 호출 실패 (" + e.getRawStatusCode() + "): " + truncate(bodyText, 600),
                    e
            );
        } catch (ResourceAccessException e) {
            // 네트워크/타임아웃
            throw new IllegalStateException("OpenAI API 네트워크 오류/타임아웃", e);
        } catch (Exception e) {
            throw new IllegalStateException("OpenAI API 호출 중 알 수 없는 오류", e);
        }
    }

    /* ---------- 유틸 ---------- */

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
}
