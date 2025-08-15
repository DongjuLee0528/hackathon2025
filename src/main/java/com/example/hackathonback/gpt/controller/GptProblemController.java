package com.example.hackathonback.gpt.controller;

import com.example.hackathonback.gpt.service.GptProblemService;
import com.example.hackathonback.problem.dto.ProblemGenerationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * GPT를 활용하여 문제를 생성하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/api/gpt") // 이 컨트롤러의 기본 URL 경로 설정
@RequiredArgsConstructor
public class GptProblemController {

    private final GptProblemService gptProblemService;

    /**
     * POST 요청을 통해 문제를 생성하는 API 엔드포인트
     * - JSON 요청만 허용, 응답은 텍스트(문제 본문)로 반환
     * - language/problemType 기본 검증
     */
    @PostMapping(
            value = "/generate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> generateProblem(@RequestBody ProblemGenerationRequestDto request) {
        // 요청 본문/필수 필드 검증
        if (request == null
                || !StringUtils.hasText(request.getLanguage())
                || !StringUtils.hasText(request.getProblemType())) {
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN)
                    .body("language, problemType는 필수입니다.");
        }

        try {
            // 문제 생성 서비스 호출
            String result = gptProblemService.generateProblem(
                    request.getLanguage().trim(),
                    request.getProblemType().trim()
            );

            if (!StringUtils.hasText(result)) {
                return ResponseEntity.internalServerError().contentType(MediaType.TEXT_PLAIN)
                        .body("문제 생성 결과가 비어 있습니다.");
            }

            // 생성된 결과를 응답으로 반환
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage() == null ? "잘못된 요청입니다." : e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().contentType(MediaType.TEXT_PLAIN)
                    .body("문제 생성 처리 중 오류가 발생했습니다.");
        }
    }
}
