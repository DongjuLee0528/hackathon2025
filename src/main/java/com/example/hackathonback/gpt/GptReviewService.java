package com.example.hackathonback.gpt;

import com.example.hackathonback.problem.client.GptApiClient;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GptReviewService {
    private final GptApiClient gptApiClient;

    public GptReviewService(GptApiClient gptApiClient) {
        this.gptApiClient = gptApiClient;
    }

    public String reviewCode(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String codeContent = Files.readString(path);

        String prompt = "다음 Java 코드를 분석해서: \n" +
                "1) 코드 요약 \n" +
                "2) 문제점 분석 \n" +
                "3) 코드 품질 개선 제안 \n" +
                "4) 개선된 코드 예시 \n" +
                "5) 테스트 용이성, 유지보수성, 가독성, 코드 품질, 구조적 복잡도 각각 0~100점으로 평가 \n" +
                "형식으로 답변해줘. \n코드:\n" + codeContent;

        return gptApiClient.getGptResponse(prompt);
    }
}