package com.example.hackathonback.analysis.controller;

import com.example.hackathonback.analysis.service.GitHubFetchService;
import org.springframework.web.bind.annotation.*;

@RestController // [한 줄 요약] GitHub 관련 API 요청을 처리하는 REST 컨트롤러
// 이 컨트롤러는 클라이언트로부터 GitHub Pull Request(PR) 관련 요청을 받아 처리합니다.

@RequestMapping("/api/github") // [한 줄 요약] 모든 요청 URL 앞에 "/api/github"을 붙여서 매핑
// 예: "/api/github/fetch/..." 형태의 URL로 접근 가능
public class GitHubFetchController {

    private final GitHubFetchService gitHubFetchService; // [한 줄 요약] GitHub API 데이터를 가져오는 서비스

    // [한 줄 요약] 서비스 의존성 주입을 위한 생성자
    // GitHubFetchService는 실제로 PR 정보를 GitHub API에서 받아오는 로직을 담당합니다.
    public GitHubFetchController(GitHubFetchService gitHubFetchService) {
        this.gitHubFetchService = gitHubFetchService;
    }

    /**
     * [한 줄 요약] 특정 PR(Pull Request)의 파일 정보를 가져오는 GET API
     *
     * 이 메서드는 GitHub 저장소의 특정 PR 번호에 해당하는 변경 파일 목록을 조회합니다.
     * 클라이언트는 저장소 소유자(owner), 저장소 이름(repo), PR 번호(prNumber), 액세스 토큰(accessToken)을 전달해야 합니다.
     *
     * 예: GET /api/github/fetch/openai/chatgpt/123?accessToken=gho_abc123...
     *
     * @param owner GitHub 저장소 소유자 (예: 사용자명 또는 조직명)
     * @param repo 저장소 이름
     * @param prNumber PR 번호
     * @param accessToken GitHub 접근 토큰 (OAuth 또는 개인 토큰)
     * @return PR에 포함된 파일 정보 문자열 (예: JSON 형식)
     */
    @GetMapping("/fetch/{owner}/{repo}/{prNumber}")
    public String fetchPullRequestFiles(@PathVariable String owner,
                                        @PathVariable String repo,
                                        @PathVariable int prNumber,
                                        @RequestParam String accessToken) {
        return gitHubFetchService.fetchPullRequestFiles(owner, repo, prNumber, accessToken);
    }
}
