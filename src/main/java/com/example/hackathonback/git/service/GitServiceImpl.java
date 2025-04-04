package com.example.hackathonback.git.service;

import com.example.hackathonback.git.client.GitApiClient;
import com.example.hackathonback.git.dto.GitRepoDto;
import com.example.hackathonback.git.entity.GitRepo;
import com.example.hackathonback.git.repository.GitRepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitServiceImpl implements GitService {

    private final GitApiClient gitApiClient;
    private final GitRepoRepository gitRepoRepository;

    @Override
    public List<GitRepoDto> getUserRepositories(String provider, String token) {
        return gitApiClient.fetchRepositories(provider, token);
    }

    // ✅ userId를 매개변수로 받아 저장
    @Override
    public void saveRepository(GitRepoDto dto, Long userId) {
        GitRepo repo = GitRepo.builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .owner(dto.getOwner())
                .provider(dto.getProvider())
                .description(dto.getDescription())
                .userId(userId)
                .build();
        gitRepoRepository.save(repo);
    }
}
