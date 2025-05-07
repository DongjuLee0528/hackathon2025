package com.example.hackathonback.gpt.service;

import com.example.hackathonback.problem.client.GptApiClient;
import org.springframework.stereotype.Service;

/**
 * GPT를 이용한 문제 생성을 담당하는 서비스 클래스
 */
@Service
public class GptProblemService {

    private final GptApiClient gptApiClient;

    /**
     * GptProblemService 생성자
     * @param gptApiClient GPT API 호출을 담당하는 클라이언트 클래스
     */
    public GptProblemService(GptApiClient gptApiClient) {
        this.gptApiClient = gptApiClient;
    }

    /**
     * 문제를 생성하기 위한 메서드
     * @param language 생성할 문제의 프로그래밍 언어
     * @param problemType 생성할 문제의 유형 (예: 정렬, 탐색 등)
     * @return GPT가 생성한 문제 내용 문자열
     */
    public String generateProblem(String language, String problemType) {
        // 언어와 문제 유형에 따라 GPT에게 전달할 프롬프트 생성
        String prompt = String.format(
                "%s 언어로 '%s' 유형의 프로그래밍 문제를 출제해줘. 문제 설명과 입력, 출력 형식, 예시를 포함해줘.",
                language, problemType
        );

        // GPT API 클라이언트를 통해 응답 받아서 반환
        return gptApiClient.getGptResponse(prompt);
    }
}
