package com.example.hackathonback.git.service;

import com.example.hackathonback.git.dto.GitRepoDto;

import java.util.List;

public interface GitService {
    List<GitRepoDto> getUserRepositories(String provider, String token);
    void saveRepository(GitRepoDto repoDto);
}
