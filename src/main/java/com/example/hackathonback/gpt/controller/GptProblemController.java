package com.example.hackathonback.gpt.controller;

import com.example.hackathonback.gpt.service.GptProblemService;
import com.example.hackathonback.problem.dto.ProblemGenerationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gpt/problem")
@RequiredArgsConstructor
public class GptProblemController {
    private final GptProblemService gptProblemService;

    @PostMapping(
            value = "/generate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> generateProblem(@RequestBody ProblemGenerationRequestDto request) {
        if (request == null
                || !StringUtils.hasText(request.getLanguage())
                || !StringUtils.hasText(request.getProblemType())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("language, problemType는 필수입니다.");
        }

        try {
            String result = gptProblemService.generateProblem(
                    request.getLanguage().trim(),
                    request.getProblemType().trim()
            );

            if (!StringUtils.hasText(result)) {
                return ResponseEntity.internalServerError()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("문제 생성 결과가 비어 있습니다.");
            }

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage() == null ? "잘못된 요청입니다." : e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("문제 생성 처리 중 오류가 발생했습니다.");
        }
    }
}
