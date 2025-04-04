package com.example.hackathonback.git.controller;

import com.example.hackathonback.git.dto.GitRepoDto;
import com.example.hackathonback.git.service.GitService;
import com.example.hackathonback.user.service.UserService; // ✅ 사용자 서비스 추가
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/git")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GitController {

    private final GitService gitService;
    private final UserService userService; // ✅ 생성자 주입

    @GetMapping("/repos")
    public List<GitRepoDto> getUserRepos(@RequestParam String provider, @RequestParam String token) {
        return gitService.getUserRepositories(provider, token);
    }

    @PostMapping("/select")
    public void selectRepository(@RequestBody GitRepoDto repoDto) {
        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // 이메일 기반 사용자 식별
        String email = oauth2User.getAttribute("email");

        // ✅ 이메일로 userId 조회
        Long userId = userService.findUserIdByEmail(email);

        // 저장 시 userId 넘기기
        gitService.saveRepository(repoDto, userId);
    }
}
