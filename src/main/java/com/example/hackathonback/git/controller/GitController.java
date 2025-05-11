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

@RestController // [한 줄 요약] Git 관련 요청을 처리하는 REST API 컨트롤러
@RequestMapping("/api/git") // [한 줄 요약] 해당 컨트롤러의 기본 URL 경로 설정
@RequiredArgsConstructor // [한 줄 요약] 생성자 주입 자동 생성 (GitService, UserService)
@CrossOrigin(origins = "*") // [한 줄 요약] 모든 출처에 대해 CORS 허용
public class GitController {

    private final GitService gitService; // [한 줄 요약] Git 리포지토리 관련 비즈니스 로직 처리 서비스
    private final UserService userService; // ✅ [한 줄 요약] 사용자 정보 조회용 서비스

    /**
     * [한 줄 요약] 사용자의 Git 리포지토리 목록 조회
     *
     * 지정된 provider(GitHub 또는 GitLab)와 OAuth 토큰을 통해
     * 사용자의 저장소 목록을 조회하여 리스트로 반환합니다.
     *
     * @param provider "github" 또는 "gitlab"
     * @param token OAuth2 액세스 토큰
     * @return GitRepoDto 리스트 (사용자 리포지토리 정보)
     */
    @GetMapping("/repos")
    public List<GitRepoDto> getUserRepos(@RequestParam String provider, @RequestParam String token) {
        return gitService.getUserRepositories(provider, token);
    }

    /**
     * [한 줄 요약] 사용자가 선택한 Git 리포지토리를 저장
     *
     * 현재 로그인한 사용자의 이메일을 기반으로 사용자 ID를 조회한 후,
     * 해당 사용자와 선택한 리포지토리 정보를 저장합니다.
     *
     * @param repoDto 클라이언트에서 전달한 리포지토리 정보
     */
    @PostMapping("/select")
    public void selectRepository(@RequestBody GitRepoDto repoDto) {
        // [한 줄 요약] 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // [한 줄 요약] OAuth2 사용자에서 이메일 추출
        String email = oauth2User.getAttribute("email");

        // [한 줄 요약] 사용자 이메일로 DB에서 ID 조회
        Long userId = userService.findUserIdByEmail(email);

        // [한 줄 요약] 사용자 ID와 리포지토리 정보 저장
        gitService.saveRepository(repoDto, userId);
    }
}
