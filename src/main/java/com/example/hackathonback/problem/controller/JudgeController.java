package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.dto.JudgeResponseDto;
import com.example.hackathonback.problem.dto.SolveRequest;
import com.example.hackathonback.problem.logic.GptJudgeLogic;
import com.example.hackathonback.problem.service.SubmissionService;
import com.example.hackathonback.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/judge")
@RequiredArgsConstructor
@Validated
public class JudgeController {

    private final GptJudgeLogic judgeLogic;
    private final SubmissionService submissionService;
    private final ProblemService problemService; // ⬅ 문제 본문 조회용

    @PostMapping(
            value = "/solve",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JudgeResponseDto> judge(@RequestBody SolveRequest req,
                                                  @AuthenticationPrincipal UserDetails user) {

        // 1) 문제 본문 조회 (problemId -> description/text)
        //    ProblemService에 getProblemText(String problemId) 메서드가 있어야 합니다.
        final String problemText = problemService.getProblemText(req.problemId());

        // 2) 채점 수행 (GptJudgeLogic의 실제 메서드명은 sendJudgeRequest)
        JudgeResponseDto result =
                judgeLogic.sendJudgeRequest(problemText, req.sourceCode(), req.language());

        // 3) 저장(옵트인)
        String userId = (user != null) ? user.getUsername() : "anonymous";
        submissionService.saveResult(
                userId,
                req.problemId(),
                req.sourceCode(),
                Boolean.TRUE.equals(req.saveRaw()),
                result
        );

        return ResponseEntity.ok(result);
    }
}
