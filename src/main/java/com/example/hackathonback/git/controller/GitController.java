package com.example.hackathonback.git.controller;

import com.example.hackathonback.git.dto.GitRepoDto;
import com.example.hackathonback.git.service.GitService;
import com.example.hackathonback.user.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Git 관련 요청을 처리하는 REST API 컨트롤러
@RequestMapping("/api/git")
@RequiredArgsConstructor // 생성자 주입 자동 생성 (GitService, UserService)
@Validated
@CrossOrigin(
        origins = {"https://emojournal.djloghub.com"},
        allowCredentials = "true"
)
public class GitController {

    private final GitService gitService;   // Git 리포지토리 관련 비즈니스 로직
    private final UserService userService; // 사용자 정보 조회용 서비스

    /**
     * 사용자의 Git 리포지토리 목록 조회
     * 권장: 토큰은 Authorization: Bearer ... 로 전달 (쿼리 파라미터 token은 하위호환)
     */
    @GetMapping(value = "/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GitRepoDto>> getUserRepos(
            @RequestParam @Pattern(regexp = "github|gitlab", message = "provider must be 'github' or 'gitlab'") String provider,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @RequestParam(name = "token", required = false) String tokenFallback
    ) {
        String token = extractBearer(authorization);
        if (token == null || token.isBlank()) token = tokenFallback;

        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }

        try {
            List<GitRepoDto> repos = gitService.getUserRepositories(provider, token);
            return ResponseEntity.ok(repos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            // 외부 API 오류/타임아웃 등
            return ResponseEntity.status(502)
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /**
     * 사용자가 선택한 Git 리포지토리 저장
     * 현재 인증된 사용자의 이메일을 기준으로 사용자 식별 → 저장
     */
    @PostMapping(value = "/select", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> selectRepository(@RequestBody GitRepoDto repoDto) {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof OAuth2User oauth2User)) {
            return ResponseEntity.status(401).build();
        }

        String email = oauth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Long userId = userService.findUserIdByEmail(email);
            gitService.saveRepository(repoDto, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /** Authorization 헤더에서 Bearer 토큰 추출 */
    private String extractBearer(String authorizationHeader) {
        if (authorizationHeader == null) return null;
        String prefix = "Bearer ";
        if (authorizationHeader.regionMatches(true, 0, prefix, 0, prefix.length())) {
            return authorizationHeader.substring(prefix.length()).trim();
        }
        return authorizationHeader.trim();
    }
}
