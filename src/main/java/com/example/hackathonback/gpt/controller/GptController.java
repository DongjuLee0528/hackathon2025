package com.example.hackathonback.gpt.controller;

import com.example.hackathonback.gpt.parser.GptResponseParser;
import com.example.hackathonback.gpt.service.GptReviewService;
import com.example.hackathonback.gpt.util.DiffUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController // REST API 컨트롤러 등록
@RequestMapping("/api/gpt") // 기본 요청 경로
@RequiredArgsConstructor // final 필드에 대해 생성자 자동 생성
public class GptController {

    private final GptReviewService gptReviewService; // GPT 기반 코드 리뷰 서비스

    /**
     * 업로드된 파일을 기반으로 GPT 코드 리뷰 요청을 처리하는 엔드포인트
     * @param file 사용자가 업로드한 코드 파일
     * @return 리뷰 요약, 문제점, 개선안, 수정 전후 코드, diff, 점수 포함한 응답
     */
    @PostMapping("/review")
    public ResponseEntity<Map<String, Object>> reviewCode(@RequestParam("file") MultipartFile file) {
        try {
            // 임시 파일 생성 후 MultipartFile 저장
            Path tempFile = Files.createTempFile("uploaded-", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());

            // GPT 리뷰 결과 받아오기
            String gptResponse = gptReviewService.reviewCode(tempFile.toString());

            // GPT 응답 파싱 (요약, 문제점, 제안, 코드 등)
            Map<String, String> parsedResult = GptResponseParser.parse(gptResponse);

            // 수정 전후 코드 diff 생성
            String diff = DiffUtil.generateDiff(parsedResult.get("beforeCode"), parsedResult.get("afterCode"));

            // 응답 객체 구성
            Map<String, Object> response = new HashMap<>();
            response.put("summary", parsedResult.get("summary"));       // 요약
            response.put("problem", parsedResult.get("problem"));       // 문제점
            response.put("suggestion", parsedResult.get("suggestion")); // 개선안
            response.put("beforeCode", parsedResult.get("beforeCode")); // 원본 코드
            response.put("afterCode", parsedResult.get("afterCode"));   // 수정 코드
            response.put("diff", diff);                                 // diff 결과

            // 각 항목별 점수 파싱 및 범위 제한
            Map<String, Integer> score = new HashMap<>();
            score.put("testability", clampScore(parsedResult.get("testability")));
            score.put("maintainability", clampScore(parsedResult.get("maintainability")));
            score.put("readability", clampScore(parsedResult.get("readability")));
            score.put("codeQuality", clampScore(parsedResult.get("codeQuality")));
            score.put("structuralComplexity", clampScore(parsedResult.get("structuralComplexity")));
            response.put("score", score); // 점수 정보 포함

            return ResponseEntity.ok(response); // 정상 응답 반환

        } catch (IOException e) {
            // 파일 처리 중 예외 발생 시 오류 응답 반환
            Map<String, Object> error = new HashMap<>();
            error.put("message", "파일 처리 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            // 기타 예외 발생 시 서버 오류 응답 반환
            Map<String, Object> error = new HashMap<>();
            error.put("message", "코드 리뷰 처리 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * 문자열로 받은 점수를 0~100 범위로 제한하여 정수로 반환
     */
    private static int clampScore(String scoreStr) {
        try {
            int score = Integer.parseInt(scoreStr);
            if (score > 100) return 100;
            if (score < 0) return 0;
            return score;
        } catch (Exception e) {
            return 0; // 숫자 변환 실패 시 0점 처리
        }
    }
}
