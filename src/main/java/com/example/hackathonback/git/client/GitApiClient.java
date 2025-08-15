package com.example.hackathonback.git.client;

import com.example.hackathonback.git.dto.GitRepoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component // GitHub/GitLab API를 호출해 사용자 리포지토리를 조회하는 클라이언트
@RequiredArgsConstructor
public class GitApiClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${app.github.base-url:https://api.github.com}")
    private String githubBaseUrl;

    @Value("${app.gitlab.base-url:https://gitlab.com/api/v4}")
    private String gitlabBaseUrl;

    /** provider 별로 사용자 리포지토리 목록 조회 */
    public List<GitRepoDto> fetchRepositories(String provider, String token) {
        if (isBlank(provider)) {
            throw new IllegalArgumentException("provider는 필수입니다. (github|gitlab)");
        }
        if (isBlank(token)) {
            throw new IllegalArgumentException("OAuth 액세스 토큰이 누락되었습니다.");
        }

        if ("github".equalsIgnoreCase(provider)) {
            return fetchFromGitHub(token);
        } else if ("gitlab".equalsIgnoreCase(provider)) {
            return fetchFromGitLab(token);
        }
        throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
    }

    /** GitHub: /user/repos (OAuth Bearer) */
    private List<GitRepoDto> fetchFromGitHub(String token) {
        WebClient client = webClientBuilder
                .baseUrl(trimSlash(githubBaseUrl))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "hackathon2025-backend")
                .build();

        // per_page=100로 최대한 많이 받아오고, 필요 시 추가 페이징은 이후 확장
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/repos")
                        .queryParam("per_page", 100)
                        .build())
                .headers(h -> h.setBearerAuth(token))
                .exchangeToMono(this::handleAsList)
                .timeout(Duration.ofSeconds(10))
                .block();
    }

    /** GitLab: /projects?membership=true (OAuth Bearer) */
    private List<GitRepoDto> fetchFromGitLab(String token) {
        WebClient client = webClientBuilder
                .baseUrl(trimSlash(gitlabBaseUrl))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/projects")
                        .queryParam("membership", true)
                        .queryParam("per_page", 100)
                        .build())
                .headers(h -> h.setBearerAuth(token))
                .exchangeToMono(this::handleAsList)
                .timeout(Duration.ofSeconds(10))
                .block();
    }

    /* ---------- 공통 처리 ---------- */

    private Mono<List<GitRepoDto>> handleAsList(ClientResponse response) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToFlux(GitRepoDto.class).collectList();
        }
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body ->
                        Mono.error(new IllegalStateException(
                                "Upstream API 오류: " + response.statusCode().value() + " " + response.statusCode()
                                        + (body.isBlank() ? "" : " - " + truncate(body, 500))
                        )));
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String trimSlash(String base) {
        if (base == null) return "";
        return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }
}
