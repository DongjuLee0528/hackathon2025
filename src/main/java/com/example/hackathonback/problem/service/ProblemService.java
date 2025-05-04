package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.client.GptApiClient;
import com.example.hackathonback.problem.entity.Problem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * GPT를 통해 코딩 문제를 자동 생성하는 서비스
 */
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final GptApiClient gptApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱용

    /**
     * GPT API를 통해 코딩 문제를 생성하고 Problem 엔티티로 매핑
     *
     * @param tag 문제 주제 (예: DFS, 정렬 등)
     * @param difficulty 난이도 (예: 초급, 중급, 고급)
     * @param language 언어 (예: java, python)
     * @return 생성된 Problem 객체
     * @throws Exception JSON 파싱 실패 또는 GPT 응답 오류
     */
    public Problem generateProblem(String tag, String difficulty, String language) throws Exception {
        String prompt = buildPrompt(tag, difficulty, language);     // 프롬프트 생성
        String gptResponse = gptApiClient.getGptResponse(prompt);  // GPT 호출

        JsonNode json = objectMapper.readTree(gptResponse); // 응답 JSON 파싱

        // 응답 내용을 Problem 엔티티에 매핑
        Problem problem = new Problem();
        problem.setTitle(json.get("title").asText());
        problem.setDescription(json.get("description").asText());
        problem.setInputFormat(json.get("inputFormat").asText());
        problem.setOutputFormat(json.get("outputFormat").asText());
        problem.setExampleInput(json.get("exampleInput").asText());
        problem.setExampleOutput(json.get("exampleOutput").asText());
        problem.setDifficulty(difficulty);
        problem.setTags(tag);

        return problem;
    }

    /**
     * GPT에게 보낼 문제 생성용 프롬프트 문자열 구성
     */
    public String buildPrompt(String tag, String difficulty, String language) {
        return String.format("""
다음과 같은 형식으로 코딩 문제를 만들어줘.
JSON 형식으로 응답해줘.

{
  "title": "문제 제목",
  "description": "문제 설명",
  "inputFormat": "입력 형식",
  "outputFormat": "출력 형식",
  "exampleInput": "예제 입력",
  "exampleOutput": "예제 출력"  
}

문제 주제: %s
난이도: %s
사용 언어: %s
""", tag, difficulty, language);
    }
}
