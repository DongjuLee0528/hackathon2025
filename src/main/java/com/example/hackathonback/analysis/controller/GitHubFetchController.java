package com.example.hackathonback.analysis.controller;

import com.example.hackathonback.analysis.service.GitHubFetchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
@Validated
public class GitHubFetchController {

    private final GitHubFetchService gitHubFetchService;

    public GitHubFetchController(GitHubFetchService gitHubFetchService) {
        this.gitHubFetchService = gitHubFetchService;
    }

    @GetMapping(value = "/fetch/{owner}/{repo}/{prNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fetchPullRequestFiles(
            @PathVariable @NotBlank String owner,
            @PathVariable @NotBlank String repo,
            @PathVariable @Positive int prNumber,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @RequestParam(name = "accessToken", required = false) String accessToken
    ) {
        String token = extractBearer(authorization);
        if (token == null || token.isBlank()) {
            token = accessToken;
        }
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"missing_token\",\"message\":\"Provide token in Authorization header (Bearer) or accessToken query param.\"}");
        }

        try {
            String result = gitHubFetchService.fetchPullRequestFiles(owner, repo, prNumber, token);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"bad_request\",\"message\":\"" + safe(e.getMessage()) + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"upstream_error\",\"message\":\"GitHub API call failed.\"}");
        }
    }

    private String extractBearer(String authorizationHeader) {
        if (authorizationHeader == null) return null;
        String prefix = "Bearer ";
        if (authorizationHeader.regionMatches(true, 0, prefix, 0, prefix.length())) {
            return authorizationHeader.substring(prefix.length()).trim();
        }
        return authorizationHeader.trim();
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }
}
