package com.example.hackathonback.config;

import com.example.hackathonback.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 로그인 성공 시
 * - AccessToken/RefreshToken 발급
 * - HttpOnly 쿠키 저장
 * - 프론트엔드로 리다이렉트
 */
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.frontend.base}")
    private String frontendBase;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        if (!(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2User not found");
            return;
        }

        //  사용자 식별자 추출 (email 우선, 없으면 GitHub id)
        String email = oAuth2User.getAttribute("email");
        String userId = (email != null) ? email : oAuth2User.getName();

        //  JWT 생성
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        //  HttpOnly 쿠키 저장
        addHttpOnlyCookie(response, "ACCESS_TOKEN", accessToken, (int) (jwtTokenProvider.getAccessTokenValidity() / 1000));
        addHttpOnlyCookie(response, "REFRESH_TOKEN", refreshToken, (int) (jwtTokenProvider.getRefreshTokenValidity() / 1000));

        //  프론트엔드 리다이렉트
        String redirectUrl = frontendBase.endsWith("/")
                ? frontendBase + "oauth/success"
                : frontendBase + "/oauth/success";
        response.sendRedirect(redirectUrl);
    }

    private void addHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS 환경에서만 전송
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
