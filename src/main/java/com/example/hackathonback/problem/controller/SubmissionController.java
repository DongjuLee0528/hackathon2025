package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.dto.CreateSubmissionDto;
import com.example.hackathonback.problem.entity.CodeSubmission;
import com.example.hackathonback.problem.service.SubmissionService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
@Validated
public class SubmissionController {

    private final SubmissionService submissionService;

    // [NEW] 제출 저장(수동 저장 API가 필요할 때 사용; judge/solve는 자동 저장)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createSubmission(@RequestBody CreateSubmissionDto dto) {
        submissionService.save(dto.userId(), dto.problemId(), dto.code(), dto.resultJson());
        return ResponseEntity.ok().build();
    }

    // 기존: 특정 사용자 제출 이력 조회
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CodeSubmission>> getUserSubmissions(@PathVariable @NotBlank String userId) {
        List<CodeSubmission> items = submissionService.getSubmissionsByUserId(userId);
        return ResponseEntity.ok(items);
    }
}
