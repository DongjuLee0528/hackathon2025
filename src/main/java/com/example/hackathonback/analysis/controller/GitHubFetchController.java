package com.example.hackathonback.analysis.controller;

import com.example.hackathonback.analysis.service.GitHubFetchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
public class GitHubFetchController {

    private final GitHubFetchService gitHubFetchService;

    public GitHubFetchController(GitHubFetchService gitHubFetchService) {
        this.gitHubFetchService = gitHubFetchService;
    }

    @GetMapping("/fetch/{owner}/{repo}/{prNumber}")
    public String fetchPullRequestFiles(@PathVariable String owner,
                                        @PathVariable String repo,
                                        @PathVariable int prNumber,
                                        @RequestParam String accessToken) {
        return gitHubFetchService.fetchPullRequestFiles(owner, repo, prNumber, accessToken);
    }
}