package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.client.GptApiClient;
import com.example.hackathonback.problem.entity.Difficulty;
import com.example.hackathonback.problem.entity.Problem;
import com.example.hackathonback.problem.entity.ProblemTag;
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
        String prompt = buildPrompt(tag, difficulty, language);
        String gptResponse = gptApiClient.getGptResponse(prompt);

        JsonNode json = objectMapper.readTree(gptResponse);

        Problem problem = new Problem();
        problem.setTitle(json.get("title").asText());
        problem.setDescription(json.get("description").asText());
        problem.setInputFormat(json.get("inputFormat").asText());
        problem.setOutputFormat(json.get("outputFormat").asText());
        problem.setExampleInput(json.get("exampleInput").asText());
        problem.setExampleOutput(json.get("exampleOutput").asText());
        problem.setDifficulty(parseDifficulty(difficulty)); // ✅ enum으로 변환
        problem.setTag(parseTag(tag));                      // ✅ enum으로 변환

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

    /**
     * 문자열 난이도를 enum Difficulty로 변환
     */
    private Difficulty parseDifficulty(String difficulty) {
        return switch (difficulty.trim().toLowerCase()) {
            case "easy", "초급" -> Difficulty.EASY;
            case "medium", "중급" -> Difficulty.MEDIUM;
            case "hard", "고급" -> Difficulty.HARD;
            default -> throw new IllegalArgumentException("지원하지 않는 난이도: " + difficulty);
        };
    }

    /**
     * 문자열 태그를 enum ProblemTag로 변환
     */
    private ProblemTag parseTag(String tag) {
        try {
            return ProblemTag.valueOf(tag.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 태그: " + tag);
        }
    }
}
