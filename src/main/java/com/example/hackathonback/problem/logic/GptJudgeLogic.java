package com.example.hackathonback.problem.logic;

import com.example.hackathonback.problem.dto.JudgeResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;  // Spring의 @Value 임포트
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GptJudgeLogic {

    @Value("${gpt.judge.api.key}")  // Spring의 @Value 어노테이션 사용
    private String apiKey;

    @Value("${gpt.judge.model}")  // Spring의 @Value 어노테이션 사용
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * GPT에 채점 요청을 보내고 응답을 파싱해 반환
     */
    public JudgeResponseDto sendJudgeRequest(String problem, String code, String lang) throws JsonProcessingException {
        String prompt = makePrompt(problem, code, lang); // 프롬프트 생성

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey); // API 키 설정
        headers.setContentType(MediaType.APPLICATION_JSON);

        // GPT API 요청 바디
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.7
        );

        // GPT 호출
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", request, String.class);

        // 응답 JSON 파싱
        JsonNode content = mapper.readTree(response.getBody())
                .path("choices").get(0)
                .path("message").path("content");

        return mapper.readValue(content.toString(), JudgeResponseDto.class);
    }

    /**
     * GPT에게 보낼 프롬프트 구성
     */
    private String makePrompt(String problem, String code, String lang) {
        return """
        다음은 문제와 사용자 코드입니다.

        문제 설명:
        %s

        사용자 코드 (%s):
        %s

        다음과 같은 JSON 형식으로 평가해주세요:
        {
            "score": 0~100,
            "feedback": "피드백 내용",
            "suggestedCode": "개선된 코드 예시"
        }
        """.formatted(problem, lang, code);
    }
}
