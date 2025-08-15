package com.example.hackathonback.analysis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service // 이 클래스는 서비스 계층의 컴포넌트로 스프링이 자동으로 관리
public class GitHubFetchService {

    private final RestTemplate restTemplate; // HTTP 요청 클라이언트
    private final String githubBaseUrl;

    public GitHubFetchService(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${app.github.base-url:https://api.github.com}") String githubBaseUrl
    ) {
        this.githubBaseUrl = githubBaseUrl;
        // 타임아웃/기본 설정
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(15))
                .build();
    }

    /**
     * GitHub API를 호출하여 특정 Pull Request의 변경 파일 목록을 가져옴
     *
     * @param owner 저장소 소유자 (예: 사용자명 또는 조직명)
     * @param repo 저장소 이름
     * @param pullNumber PR 번호(양수)
     * @param accessToken GitHub 액세스 토큰 (OAuth 또는 PAT) — 권장: Bearer
     * @return PR에 포함된 파일 목록(JSON 형식 문자열)
     */
    public String fetchPullRequestFiles(String owner, String repo, int pullNumber, String accessToken) {
        // 기본 파라미터 검증
        if (isBlank(owner) || isBlank(repo) || pullNumber <= 0) {
            throw new IllegalArgumentException("owner/repo/pullNumber가 유효하지 않습니다.");
        }
        if (isBlank(accessToken)) {
            throw new IllegalArgumentException("GitHub 액세스 토큰이 누락되었습니다.");
        }

        // GitHub API URL 구성 (페이지당 최대 100개)
        String url = String.format("%s/repos/%s/%s/pulls/%d/files?per_page=100",
                trimTrailingSlash(githubBaseUrl), owner, repo, pullNumber);

        // Authorization/Accept 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.trim());
        headers.set(HttpHeaders.ACCEPT, "application/vnd.github+json");
        headers.set(HttpHeaders.USER_AGENT, "hackathon2025-backend"); // 일부 환경에서 필요

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody() == null ? "[]" : response.getBody();
            }
            // 비정상 상태코드 처리
            throw new IllegalStateException("GitHub API 응답 오류: " + response.getStatusCode());

        } catch (RestClientResponseException e) {
            // 4xx/5xx 응답 본문 포함
            String body = e.getResponseBodyAsString();
            String msg = "GitHub API 호출 실패 (" + e.getRawStatusCode() + "): " + (body == null ? "" : body);
            throw new IllegalStateException(msg, e);
        } catch (ResourceAccessException e) {
            // 타임아웃/네트워크 접근 문제
            throw new IllegalStateException("GitHub API 네트워크 오류/타임아웃", e);
        } catch (Exception e) {
            // 기타 예외
            throw new IllegalStateException("GitHub API 호출 중 알 수 없는 오류", e);
        }
    }

    /* ---------- 내부 유틸 ---------- */

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String trimTrailingSlash(String base) {
        if (base == null) return "";
        return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }
}
