package com.example.hackathonback.git.client;

import com.example.hackathonback.git.dto.GitRepoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component // 스프링이 관리하는 Bean으로 등록되는 컴포넌트
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성 (DI 용도)
public class GitApiClient {

    private final WebClient.Builder webClientBuilder; // WebClient 생성을 위한 빌더

    /**
     * Git provider(github/gitlab)에 따라 리포지토리 목록을 조회
     *
     * @param provider "github" 또는 "gitlab"
     * @param token OAuth 액세스 토큰
     * @return 리포지토리 DTO 리스트
     */
    public List<GitRepoDto> fetchRepositories(String provider, String token) {
        if ("github".equalsIgnoreCase(provider)) {
            return fetchFromGitHub(token);
        } else if ("gitlab".equalsIgnoreCase(provider)) {
            return fetchFromGitLab(token);
        }
        throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
    }

    /**
     * GitHub에서 사용자 리포지토리 조회
     */
    private List<GitRepoDto> fetchFromGitHub(String token) {
        return webClientBuilder.build()
                .get()
                .uri("https://api.github.com/user/repos") // GitHub API URL
                .headers(h -> h.setBearerAuth(token)) // 인증 헤더 설정
                .retrieve() // HTTP 요청 실행
                .onStatus(
                        status -> status.isError(), // 오류 상태일 경우
                        response -> response.bodyToMono(String.class).map(RuntimeException::new) // 예외로 매핑
                )
                .bodyToFlux(GitRepoDto.class) // 응답 바디를 Flux<GitRepoDto>로 매핑
                .collectList() // 리스트로 변환
                .block(); // 동기적으로 결과 대기
    }

    /**
     * GitLab에서 사용자 리포지토리 조회
     */
    private List<GitRepoDto> fetchFromGitLab(String token) {
        return webClientBuilder.build()
                .get()
                .uri("https://gitlab.com/api/v4/projects?membership=true") // GitLab API URL
                .headers(h -> h.setBearerAuth(token)) // 인증 헤더 설정
                .retrieve()
                .onStatus(
                        status -> status.isError(), // 오류 상태 핸들링
                        response -> response.bodyToMono(String.class).map(RuntimeException::new)
                )
                .bodyToFlux(GitRepoDto.class)
                .collectList()
                .block();
    }
}
