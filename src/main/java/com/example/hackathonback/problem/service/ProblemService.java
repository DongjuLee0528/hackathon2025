package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.client.GptApiClient;
import com.example.hackathonback.problem.entity.Problem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final GptApiClient gptApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        problem.setDifficulty(difficulty);
        problem.setTags(tag);

        return problem;
    }

    public String buildPrompt(String tag, String difficulty, String language) {
        return String.format("""
다음과 같은 형식으로 코딩 문제를 만들어줘.
JSON 형식으로 응답해줘.

{
  \"title\": \"문제 제목\",
  \"description\": \"문제 설명\",
  \"inputFormat\": \"입력 형식\",
  \"outputFormat\": \"출력 형식\",
  \"exampleInput\": \"예제 입력\",
  \"exampleOutput\": \"예제 출력\"  
}

문제 주제: %s
난이도: %s
사용 언어: %s
""", tag, difficulty, language);
    }
}
