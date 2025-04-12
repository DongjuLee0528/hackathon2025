
package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.entity.CodeSubmission;
import com.example.hackathonback.problem.service.SubmissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/{userId}")
    public List<CodeSubmission> getUserSubmissions(@PathVariable String userId) {
        return submissionService.getSubmissionsByUserId(userId);
    }
}
