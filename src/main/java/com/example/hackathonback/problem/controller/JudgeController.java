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
@RequestMapping("/api/judge")
@RequiredArgsConstructor
@Validated
public class JudgeController {

    private final GptJudgeLogic judgeLogic;
    private final SubmissionService submissionService;
    private final ProblemService problemService;

    @PostMapping(
            value = "/solve",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JudgeResponseDto> judge(@RequestBody SolveRequest req,
                                                  @AuthenticationPrincipal UserDetails user) {

        String problemText = problemService.getProblemText(req.problemId());
        JudgeResponseDto result = judgeLogic.sendJudgeRequest(problemText, req.sourceCode(), req.language());

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
