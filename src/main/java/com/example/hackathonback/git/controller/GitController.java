package com.example.hackathonback.git.controller;

import com.example.hackathonback.git.dto.GitRepoDto;
import com.example.hackathonback.git.service.GitService;
import com.example.hackathonback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/git")
@RequiredArgsConstructor
@Validated
public class GitController {

    private final GitService gitService;
    private final UserService userService;

    @GetMapping(value = "/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserRepos(
            @RequestParam @Pattern(regexp = "github|gitlab", message = "provider must be 'github' or 'gitlab'") String provider,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @RequestParam(name = "token", required = false) String tokenFallback
    ) {
        String token = extractBearer(authorization);
        if (token == null || token.isBlank()) token = tokenFallback;

        if (token == null || token.isBlank()) {
            return badRequest("OAuth 액세스 토큰이 필요합니다.");
        }

        try {
            List<GitRepoDto> repos = gitService.getUserRepositories(provider, token);
            return ResponseEntity.ok(repos);
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage() == null ? "요청 파라미터가 올바르지 않습니다." : e.getMessage());
        } catch (Exception e) {
            return upstreamError("외부 Git API 호출에 실패했습니다.");
        }
    }

    @PostMapping(value = "/select", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> selectRepository(@RequestBody GitRepoDto repoDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message("인증이 필요합니다."));
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof OAuth2User oauth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message("OAuth2 사용자만 허용됩니다."));
        }

        String email = oauth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            return badRequest("이메일 정보를 확인할 수 없습니다.");
        }

        try {
            Long userId = userService.findUserIdByEmail(email);
            gitService.saveRepository(repoDto, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage() == null ? "요청 데이터가 올바르지 않습니다." : e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message("서버 내부 오류가 발생했습니다."));
        }
    }

    private String extractBearer(String authorizationHeader) {
        if (authorizationHeader == null) return null;
        String prefix = "Bearer ";
        if (authorizationHeader.regionMatches(true, 0, prefix, 0, prefix.length())) {
            return authorizationHeader.substring(prefix.length()).trim();
        }
        return authorizationHeader.trim();
    }

    private ResponseEntity<Map<String, String>> badRequest(String msg) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(message(msg));
    }

    private ResponseEntity<Map<String, String>> upstreamError(String msg) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(message(msg));
    }

    private Map<String, String> message(String msg) {
        Map<String, String> m = new HashMap<>();
        m.put("message", msg);
        return m;
    }
}
