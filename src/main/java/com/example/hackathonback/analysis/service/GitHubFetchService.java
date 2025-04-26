package com.example.hackathonback.analysis.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubFetchService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchPullRequestFiles(String owner, String repo, int pullNumber, String accessToken) {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d/files", owner, repo, pullNumber);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}