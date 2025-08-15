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

@RestController // REST API 컨트롤러 등록
@RequestMapping("/api/gpt") // 기본 요청 경로
@RequiredArgsConstructor // final 필드에 대해 생성자 자동 생성
@Validated
public class GptController {

    private final GptReviewService gptReviewService; // GPT 기반 코드 리뷰 서비스

    /**
     * 업로드된 파일을 기반으로 GPT 코드 리뷰 요청을 처리하는 엔드포인트
     * - 멀티파트 전송만 허용
     * - 빈 파일/비지원 콘텐츠 타입 방지
     * - 임시 파일은 처리 후 반드시 삭제
     */
    @PostMapping(
            value = "/review",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> reviewCode(@RequestParam("file") @NotNull MultipartFile file) {
        // 기본 검증: 비어 있음/파일명 없음/콘텐츠 타입 제한(선택)
        if (file.isEmpty() || !StringUtils.hasText(file.getOriginalFilename())) {
            return badRequest("유효한 파일이 아닙니다.");
        }
        // 필요 시 화이트리스트 확장 (예: text/*, application/json 등)
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.startsWith("text/")
                || MediaType.APPLICATION_JSON_VALUE.equals(contentType)
                || "application/octet-stream".equals(contentType))) {
            return badRequest("지원하지 않는 파일 형식입니다.");
        }

        Path tempFile = null;
        try {
            // 사용자 파일명은 임시 파일 접미사로 사용하지 않음(경로 인젝션/OS 특수문자 회피)
            tempFile = Files.createTempFile("uploaded-", ".tmp");
            file.transferTo(tempFile.toFile());

            // GPT 리뷰 결과 받아오기
            String gptResponse = gptReviewService.reviewCode(tempFile.toString());

            // GPT 응답 파싱 (요약, 문제점, 제안, 코드 등)
            Map<String, String> parsed = GptResponseParser.parse(gptResponse);

            // 널/결측값 방어
            String beforeCode = parsed.getOrDefault("beforeCode", "");
            String afterCode  = parsed.getOrDefault("afterCode", "");
            String diff = DiffUtil.generateDiff(beforeCode, afterCode);

            // 응답 객체 구성
            Map<String, Object> response = new HashMap<>();
            response.put("summary", parsed.getOrDefault("summary", ""));
            response.put("problem", parsed.getOrDefault("problem", ""));
            response.put("suggestion", parsed.getOrDefault("suggestion", ""));
            response.put("beforeCode", beforeCode);
            response.put("afterCode", afterCode);
            response.put("diff", diff);

            // 점수(0~100 클램프)
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
            // 임시 파일은 반드시 정리
            if (tempFile != null) {
                try { Files.deleteIfExists(tempFile); } catch (Exception ignore) {}
            }
        }
    }

    /** 문자열로 받은 점수를 0~100 범위로 제한하여 정수로 반환 */
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
