package com.example.hackathonback.gpt.service;

import com.example.hackathonback.problem.client.GptApiClient;
import org.springframework.stereotype.Service;

@Service
public class GptProblemService {

    private final GptApiClient gptApiClient;

    public GptProblemService(GptApiClient gptApiClient) {
        this.gptApiClient = gptApiClient;
    }

    public String generateProblem(String language, String problemType) {
        String prompt = String.format(
            "%s 언어로 '%s' 유형의 프로그래밍 문제를 출제해줘. 문제 설명과 입력, 출력 형식, 예시를 포함해줘.",
            language, problemType
        );
        return gptApiClient.getGptResponse(prompt);
    }
}