package com.example.hackathonback.problem.client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * OpenAI GPT API에 요청을 보내는 클라이언트 컴포넌트
 */
@Component
public class GptApiClient {

    // 환경 변수에서 API 키 및 모델명 가져오기
    private final String apiKey = System.getenv("GPT_PROBLEM_KEY");
    private final String model = System.getenv("GPT_PROBLEM_MODEL");

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * GPT에게 사용자 프롬프트를 전송하고 응답 내용을 반환
     *
     * @param prompt 사용자 질문 또는 요청 메시지
     * @return GPT 응답 내용 (String)
     */
    public String getGptResponse(String prompt) {
        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey); // Authorization: Bearer {API_KEY}
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 구성 (ChatGPT 메시지 형식)
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)), // 사용자 메시지 구성
                "temperature", 0.7 // 응답 다양성 제어
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // GPT API 호출 (POST /v1/chat/completions)
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                request,
                Map.class
        );

        // 응답에서 첫 번째 메시지의 "content" 추출
        Map choice = (Map) ((List) response.getBody().get("choices")).get(0);
        Map message = (Map) choice.get("message");

        return (String) message.get("content"); // 사용자에게 반환할 응답 내용
    }
}
