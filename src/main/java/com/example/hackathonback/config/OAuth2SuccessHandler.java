package com.example.hackathonback.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 로그인 성공 시
 * - 별도 토큰 발급 없음
 * - 프론트엔드로 바로 리다이렉트
 */
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    // 프론트엔드 성공 리다이렉트 URL (application.properties 에 정의)
    private final String frontendRedirect = "http://localhost:3000/oauth/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        if (!(authentication.getPrincipal() instanceof OAuth2User)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2User not found");
            return;
        }

        // ✅ JWT, 쿠키 저장 없이 단순히 프론트로 리다이렉트만
        response.sendRedirect(frontendRedirect);
    }
}
