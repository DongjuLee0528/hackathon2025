package com.example.hackathonback.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptReviewService gptReviewService;

    @PostMapping("/review")
    public ResponseEntity<Map<String, Object>> reviewCode(@RequestParam("file") MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile("uploaded-", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());

            String gptResponse = gptReviewService.reviewCode(tempFile.toString());
            Map<String, String> parsedResult = GptResponseParser.parse(gptResponse);
            String diff = DiffUtil.generateDiff(parsedResult.get("beforeCode"), parsedResult.get("afterCode"));

            Map<String, Object> response = new HashMap<>();
            response.put("summary", parsedResult.get("summary"));
            response.put("problem", parsedResult.get("problem"));
            response.put("suggestion", parsedResult.get("suggestion"));
            response.put("beforeCode", parsedResult.get("beforeCode"));
            response.put("afterCode", parsedResult.get("afterCode"));
            response.put("diff", diff);

            Map<String, Integer> score = new HashMap<>();
            score.put("testability", clampScore(parsedResult.get("testability")));
            score.put("maintainability", clampScore(parsedResult.get("maintainability")));
            score.put("readability", clampScore(parsedResult.get("readability")));
            score.put("codeQuality", clampScore(parsedResult.get("codeQuality")));
            score.put("structuralComplexity", clampScore(parsedResult.get("structuralComplexity")));
            response.put("score", score);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "파일 처리 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "코드 리뷰 처리 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    private static int clampScore(String scoreStr) {
        try {
            int score = Integer.parseInt(scoreStr);
            if (score > 100) return 100;
            if (score < 0) return 0;
            return score;
        } catch (Exception e) {
            return 0;
        }
    }
}