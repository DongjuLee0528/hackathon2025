package com.example.hackathonback.git.client;

import com.example.hackathonback.git.dto.GitRepoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GitApiClient {

    private final WebClient.Builder webClientBuilder;

    public List<GitRepoDto> fetchRepositories(String provider, String token) {
        if ("github".equalsIgnoreCase(provider)) {
            return fetchFromGitHub(token);
        } else if ("gitlab".equalsIgnoreCase(provider)) {
            return fetchFromGitLab(token);
        }
        throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
    }

    private List<GitRepoDto> fetchFromGitHub(String token) {
        return webClientBuilder.build()
                .get()
                .uri("https://api.github.com/user/repos")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class).map(RuntimeException::new)
                )
                .bodyToFlux(GitRepoDto.class)
                .collectList()
                .block();
    }

    private List<GitRepoDto> fetchFromGitLab(String token) {
        return webClientBuilder.build()
                .get()
                .uri("https://gitlab.com/api/v4/projects?membership=true")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class).map(RuntimeException::new)
                )
                .bodyToFlux(GitRepoDto.class)
                .collectList()
                .block();
    }
}
