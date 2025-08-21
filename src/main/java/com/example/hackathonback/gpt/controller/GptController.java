package com.example.hackathonback.gpt.controller;

import com.example.hackathonback.gpt.parser.GptResponseParser;
import com.example.hackathonback.gpt.service.GptReviewService;
import com.example.hackathonback.gpt.util.DiffUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class GptController {

    private final GptReviewService gptReviewService;

    @PostMapping(
            value = "/review",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> reviewCode(@RequestParam("file") @NotNull MultipartFile file) {
        if (file.isEmpty() || !StringUtils.hasText(file.getOriginalFilename())) {
            return badRequest("유효한 파일이 아닙니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !(contentType.startsWith("text/")
                || MediaType.APPLICATION_JSON_VALUE.equals(contentType)
                || "application/octet-stream".equals(contentType))) {
            return badRequest("지원하지 않는 파일 형식입니다.");
        }

        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("uploaded-", ".tmp");
            file.transferTo(tempFile.toFile());

            String gptResponse = gptReviewService.reviewCode(tempFile.toString());
            Map<String, String> parsed = GptResponseParser.parse(gptResponse);

            String beforeCode = parsed.getOrDefault("beforeCode", "");
            String afterCode  = parsed.getOrDefault("afterCode", "");
            String diff = DiffUtil.generateDiff(beforeCode, afterCode);

            Map<String, Object> response = new HashMap<>();
            response.put("summary", parsed.getOrDefault("summary", ""));
            response.put("problem", parsed.getOrDefault("problem", ""));
            response.put("suggestion", parsed.getOrDefault("suggestion", ""));
            response.put("beforeCode", beforeCode);
            response.put("afterCode", afterCode);
            response.put("diff", diff);

            Map<String, Integer> score = new HashMap<>();
            score.put("testability", clampScore(parsed.get("testability")));
            score.put("maintainability", clampScore(parsed.get("maintainability")));
            score.put("readability", clampScore(parsed.get("readability")));
            score.put("codeQuality", clampScore(parsed.get("codeQuality")));
            score.put("structuralComplexity", clampScore(parsed.get("structuralComplexity")));
            response.put("score", score);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return badRequest("파일 처리 중 오류가 발생했습니다.");
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage() == null ? "잘못된 요청입니다." : e.getMessage());
        } catch (Exception e) {
            return serverError("코드 리뷰 처리 중 오류가 발생했습니다.");
        } finally {
            if (tempFile != null) {
                try { Files.deleteIfExists(tempFile); } catch (Exception ignore) {}
            }
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

    private ResponseEntity<Map<String, Object>> badRequest(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(body);
    }

    private ResponseEntity<Map<String, Object>> serverError(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
