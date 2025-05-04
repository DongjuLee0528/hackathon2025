package com.example.hackathonback.gpt.service;

import com.example.hackathonback.problem.client.GptApiClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * GPT에게 코드 리뷰 요청을 보내는 서비스 클래스
 */
@Service
public class GptReviewService {

    private final GptApiClient gptApiClient;

    // 생성자를 통한 GptApiClient 의존성 주입
    public GptReviewService(GptApiClient gptApiClient) {
        this.gptApiClient = gptApiClient;
    }

    /**
     * 파일 경로로부터 코드 내용을 읽고 GPT에게 리뷰 요청을 보냄
     *
     * @param filePath 업로드된 코드 파일 경로
     * @return GPT로부터 받은 리뷰 응답 (문자열)
     * @throws IOException 파일 읽기 실패 시 예외 발생
     */
    public String reviewCode(String filePath) throws IOException {
        // 파일 경로로부터 코드 내용 읽기
        Path path = Paths.get(filePath);
        String codeContent = Files.readString(path);

        // GPT 프롬프트 구성
        String prompt = "다음 Java 코드를 분석해서: \n" +
                "1) 코드 요약 \n" +
                "2) 문제점 분석 \n" +
                "3) 코드 품질 개선 제안 \n" +
                "4) 개선된 코드 예시 \n" +
                "5) 테스트 용이성, 유지보수성, 가독성, 코드 품질, 구조적 복잡도 각각 0~100점으로 평가 \n" +
                "형식으로 답변해줘. \n코드:\n" + codeContent;

        // GPT API 호출
        return gptApiClient.getGptResponse(prompt);
    }
}
