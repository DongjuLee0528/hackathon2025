package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.entity.CodeSubmission;
import com.example.hackathonback.problem.service.SubmissionService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API 컨트롤러로 등록
@RequestMapping("/submissions") // 제출 기록 관련 API 기본 경로 (기존 경로 유지)
@RequiredArgsConstructor
@Validated
public class SubmissionController {

    private final SubmissionService submissionService;

    /**
     * 특정 사용자의 제출 기록을 조회
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 CodeSubmission 리스트
     */
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CodeSubmission>> getUserSubmissions(@PathVariable @NotBlank String userId) {
        List<CodeSubmission> items = submissionService.getSubmissionsByUserId(userId);
        return ResponseEntity.ok(items);
    }
}
