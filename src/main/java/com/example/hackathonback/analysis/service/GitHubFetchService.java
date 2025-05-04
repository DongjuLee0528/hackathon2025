package com.example.hackathonback.analysis.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service // 이 클래스는 서비스 계층의 컴포넌트로 스프링이 자동으로 관리
public class GitHubFetchService {

    private final RestTemplate restTemplate = new RestTemplate(); // HTTP 요청을 보내기 위한 RestTemplate 객체

    /**
     * GitHub API를 호출하여 특정 Pull Request의 변경 파일 목록을 가져옴
     *
     * @param owner 저장소 소유자 (예: 사용자명 또는 조직명)
     * @param repo 저장소 이름
     * @param pullNumber PR 번호
     * @param accessToken GitHub 액세스 토큰 (OAuth 또는 PAT)
     * @return PR에 포함된 파일 목록(JSON 형식 문자열)
     */
    public String fetchPullRequestFiles(String owner, String repo, int pullNumber, String accessToken) {
        // GitHub API URL 구성
        String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d/files", owner, repo, pullNumber);

        // Authorization 헤더 설정 (Personal Access Token 사용)
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "token " + accessToken);

        // 요청 엔티티 생성 (헤더 포함)
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

        // GET 요청 수행
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                entity,
                String.class
        );

        // 응답 본문 반환 (PR 파일 목록 JSON)
        return response.getBody();
    }
}
