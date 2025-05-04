package com.example.hackathonback.analysis.controller;

import com.example.hackathonback.analysis.service.GitHubFetchService;
import org.springframework.web.bind.annotation.*;

@RestController // 이 클래스가 REST API 컨트롤러임을 나타냄
@RequestMapping("/api/github") // 이 컨트롤러의 기본 URL 경로 설정
public class GitHubFetchController {

    private final GitHubFetchService gitHubFetchService;

    // 생성자를 통해 GitHubFetchService 의존성 주입
    public GitHubFetchController(GitHubFetchService gitHubFetchService) {
        this.gitHubFetchService = gitHubFetchService;
    }

    /**
     * 특정 GitHub PR(Pull Request)의 파일 정보를 가져오는 GET 요청 처리
     * GET /api/github/fetch/{owner}/{repo}/{prNumber}?accessToken=...
     *
     * @param owner GitHub 저장소 소유자 (예: 사용자명 또는 조직명)
     * @param repo 저장소 이름
     * @param prNumber PR 번호
     * @param accessToken GitHub 접근 토큰 (OAuth 또는 PAT)
     * @return PR에 포함된 파일 정보 (문자열 형식, JSON 또는 기타 텍스트)
     */
    @GetMapping("/fetch/{owner}/{repo}/{prNumber}")
    public String fetchPullRequestFiles(@PathVariable String owner,
                                        @PathVariable String repo,
                                        @PathVariable int prNumber,
                                        @RequestParam String accessToken) {
        return gitHubFetchService.fetchPullRequestFiles(owner, repo, prNumber, accessToken);
    }
}
