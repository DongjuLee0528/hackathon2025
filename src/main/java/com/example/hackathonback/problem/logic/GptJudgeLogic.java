package com.example.hackathonback.problem.logic;

import com.example.hackathonback.problem.dto.JudgeResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * GPT API를 이용하여 사용자 코드를 자동 채점하는 로직 클래스
 */
@Component
public class GptJudgeLogic {

    @Value("${gpt.judge.api.key}") // 환경 변수 또는 application.yml에서 주입
    private String apiKey;

    @Value("${gpt.judge.model}") // 예: gpt-3.5-turbo 또는 gpt-4
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * GPT에게 문제와 사용자 코드를 기반으로 채점 요청을 보냄
     *
     * @param problem 문제 설명 (텍스트)
     * @param code 사용자 제출 코드
     * @param lang 사용 언어 (예: java, python)
     * @return GPT가 반환한 채점 결과 DTO
     */
    public JudgeResponseDto sendJudgeRequest(String problem, String code, String lang) throws JsonProcessingException {
        String prompt = makePrompt(problem, code, lang); // GPT 프롬프트 생성

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 구성
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // GPT API 호출
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", request, String.class
        );

        // 응답 JSON에서 content 추출
        JsonNode content = mapper.readTree(response.getBody())
                .path("choices").get(0)
                .path("message").path("content");

        // content를 JudgeResponseDto로 매핑
        JudgeResponseDto result = mapper.readValue(content.toString(), JudgeResponseDto.class);

        // 추천 코드가 비어있으면 기본값 설정
        if (result.getSuggestedCode() == null || result.getSuggestedCode().trim().isEmpty()) {
            result.setSuggestedCode("// 추천 코드 없음");
        }

        return result;
    }

    /**
     * GPT에게 전달할 프롬프트 문자열 생성
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
  "suggestedCode": "개선된 코드 예시 (없다면 생략하거나 빈 문자열로)"
}
""".formatted(problem, lang, code);
    }
}
