package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.entity.CodeSubmission;
import com.example.hackathonback.problem.service.SubmissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API 컨트롤러로 등록
@RequestMapping("/submissions") // 제출 기록 관련 API 기본 경로
public class SubmissionController {

    private final SubmissionService submissionService;

    // 생성자를 통한 서비스 주입
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    /**
     * 특정 사용자의 제출 기록을 조회
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 CodeSubmission 리스트
     */
    @GetMapping("/{userId}")
    public List<CodeSubmission> getUserSubmissions(@PathVariable String userId) {
        return submissionService.getSubmissionsByUserId(userId);
    }
}
