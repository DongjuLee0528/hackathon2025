package com.example.hackathonback.git.client;

import com.example.hackathonback.git.dto.GitRepoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component // [한 줄 요약] GitHub/GitLab API를 호출해 사용자 리포지토리를 조회하는 클라이언트
// 이 컴포넌트는 WebClient를 통해 Git API를 호출하고, 사용자의 저장소 목록을 가져오는 역할을 합니다.
@RequiredArgsConstructor // [한 줄 요약] final 필드 주입을 위한 생성자 자동 생성 (DI 용도)
public class GitApiClient {

    private final WebClient.Builder webClientBuilder; // [한 줄 요약] WebClient 인스턴스 생성을 위한 빌더

    /**
     * [한 줄 요약] GitHub 또는 GitLab에서 사용자 리포지토리 목록을 조회
     *
     * provider 파라미터에 따라 GitHub 또는 GitLab API를 호출하여 해당 사용자의 리포지토리를 가져옵니다.
     *
     * @param provider "github" 또는 "gitlab"
     * @param token OAuth 액세스 토큰
     * @return GitRepoDto 객체 리스트 (사용자 리포지토리 정보)
     */
    public List<GitRepoDto> fetchRepositories(String provider, String token) {
        if ("github".equalsIgnoreCase(provider)) {
            return fetchFromGitHub(token);
        } else if ("gitlab".equalsIgnoreCase(provider)) {
            return fetchFromGitLab(token);
        }
        throw new IllegalArgumentException("지원하지 않는 provider: " + provider); // 잘못된 provider 처리
    }

    /**
     * [한 줄 요약] GitHub API를 호출하여 사용자 리포지토리 목록 조회
     *
     * GitHub OAuth 토큰을 Authorization 헤더에 포함시켜 인증하고,
     * `/user/repos` 엔드포인트에서 사용자의 리포지토리 정보를 가져옵니다.
     *
     * @param token GitHub OAuth 액세스 토큰
     * @return GitRepoDto 리스트
     */
    private List<GitRepoDto> fetchFromGitHub(String token) {
        return webClientBuilder.build()
                .get()
                .uri("https://api.github.com/user/repos") // GitHub API URL
                .headers(h -> h.setBearerAuth(token)) // Bearer 토큰 헤더 추가
                .retrieve() // HTTP 요청 실행
                .onStatus(
                        status -> status.isError(), // 오류 응답 처리
                        response -> response.bodyToMono(String.class).map(RuntimeException::new)
                )
                .bodyToFlux(GitRepoDto.class) // 응답을 Flux<GitRepoDto>로 변환
                .collectList() // Flux를 List로 변환
                .block(); // 동기 방식으로 결과 대기
    }

    /**
     * [한 줄 요약] GitLab API를 호출하여 사용자 리포지토리 목록 조회
     *
     * GitLab OAuth 토큰을 Bearer 인증 방식으로 보내고,
     * `projects?membership=true` 엔드포인트를 통해 리포지토리를 가져옵니다.
     *
     * @param token GitLab OAuth 액세스 토큰
     * @return GitRepoDto 리스트
     */
    private List<GitRepoDto> fetchFromGitLab(String token) {
        return webClientBuilder.build()
                .get()
                .uri("https://gitlab.com/api/v4/projects?membership=true") // GitLab API URL
                .headers(h -> h.setBearerAuth(token)) // 인증 토큰 설정
                .retrieve()
                .onStatus(
                        status -> status.isError(), // 에러 상태 처리
                        response -> response.bodyToMono(String.class).map(RuntimeException::new)
                )
                .bodyToFlux(GitRepoDto.class)
                .collectList()
                .block();
    }
}
