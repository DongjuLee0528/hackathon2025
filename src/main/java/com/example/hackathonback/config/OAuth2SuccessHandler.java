package com.example.hackathonback.config;

import com.example.hackathonback.jwt.JwtTokenProvider;
import com.example.hackathonback.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

/**
 * OAuth2 로그인 성공 시
 * - 사용자 보장(없으면 생성)
 * - 이메일을 subject로 JWT 발급
 * - HttpOnly 쿠키 저장(SameSite=None; Secure; Domain=.djloghub.com)
 * - 프론트엔드로 리다이렉트
 */
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Value("${app.frontend.base}")
    private String frontendBase;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(
            jakarta.servlet.http.HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        if (!(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2User not found");
            return;
        }

        // GitHub 프로필 속성 파싱
        Map<String, Object> attrs = oAuth2User.getAttributes();
        String email   = (String) attrs.get("email");                        // 공개 이메일 없을 수 있음
        String login   = (String) attrs.getOrDefault("login", "github-user");
        String name    = (String) attrs.getOrDefault("name", login);
        String avatar  = (String) attrs.get("avatar_url");
        Long githubId  = attrs.get("id") instanceof Number ? ((Number) attrs.get("id")).longValue() : null;

        // 사용자 보장(없으면 생성)
        userService.ensureUserExistsByGithub(githubId, login, name, email, avatar);

        // JWT subject는 반드시 "이메일"
        if (email == null || email.isBlank()) {
            email = login + "@users.noreply.github.com";
        }
        String accessToken  = jwtTokenProvider.createAccessToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        // HttpOnly 쿠키 저장 (SameSite=None; Secure; Domain=.djloghub.com)
        addHttpOnlyCookie(response, "ACCESS_TOKEN",  accessToken,  (int) (jwtTokenProvider.getAccessTokenValidity()   / 1000));
        addHttpOnlyCookie(response, "REFRESH_TOKEN", refreshToken, (int) (jwtTokenProvider.getRefreshTokenValidity() / 1000));

        // 프론트로 리다이렉트
        String redirectUrl = frontendBase.endsWith("/") ? frontendBase + "oauth/success" : frontendBase + "/oauth/success";
        response.sendRedirect(redirectUrl);
    }

    private void addHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .domain(".djloghub.com")
                .sameSite("None")
                .maxAge(Duration.ofSeconds(maxAgeSeconds))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
