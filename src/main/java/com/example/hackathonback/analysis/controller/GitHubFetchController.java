package com.example.hackathonback.analysis.controller;

import com.example.hackathonback.analysis.service.GitHubFetchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController // [한 줄 요약] GitHub 관련 API 요청을 처리하는 REST 컨트롤러
@RequestMapping("/api/github") // [한 줄 요약] 모든 요청 URL 앞에 "/api/github"을 붙여서 매핑
@Validated
public class GitHubFetchController {

    private final GitHubFetchService gitHubFetchService; // [한 줄 요약] GitHub API 데이터를 가져오는 서비스

    // [한 줄 요약] 서비스 의존성 주입을 위한 생성자
    public GitHubFetchController(GitHubFetchService gitHubFetchService) {
        this.gitHubFetchService = gitHubFetchService;
    }

    /**
     * [한 줄 요약] 특정 PR(Pull Request)의 파일 정보를 가져오는 GET API
     *
     * 권장: 토큰은 쿼리스트링 대신 Authorization 헤더로 전달 (보안)
     * 예:
     *   GET /api/github/fetch/openai/chatgpt/123
     *   Authorization: Bearer ghp_xxx...
     *
     * (하위호환) accessToken 쿼리 파라미터도 허용하되 비권장
     *
     * @param owner GitHub 저장소 소유자
     * @param repo 저장소 이름
     * @param prNumber PR 번호(양수)
     * @param authorization Authorization 헤더(Bearer 토큰)
     * @param accessToken (비권장) 쿼리 파라미터 토큰
     */
    @GetMapping(value = "/fetch/{owner}/{repo}/{prNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fetchPullRequestFiles(
            @PathVariable @NotBlank String owner,
            @PathVariable @NotBlank String repo,
            @PathVariable @Positive int prNumber,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @RequestParam(name = "accessToken", required = false) String accessToken
    ) {
        // 토큰 소스 결정: 헤더 우선, 없으면 쿼리 파라미터
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
            // 서비스에서 파라미터 검증 실패 등
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"bad_request\",\"message\":\"" + safe(e.getMessage()) + "\"}");
        } catch (Exception e) {
            // 네트워크 오류/타임아웃/기타 예외
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

    // JSON 응답에 쓸 간단한 이스케이프 (필요 최소한)
    private String safe(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }
}
