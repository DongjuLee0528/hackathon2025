package com.example.hackathonback.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${app.frontend.failure-redirect:https://thecoder.djloghub.com/oauth/failure}")
    private String frontendFailureRedirect;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        log.warn("OAuth2 로그인 실패: {}", exception.getMessage());

        String reason = URLEncoder.encode("oauth_failed", StandardCharsets.UTF_8);
        String target = frontendFailureRedirect.contains("?")
                ? frontendFailureRedirect + "&error=" + reason
                : frontendFailureRedirect + "?error=" + reason;

        response.sendRedirect(target);
    }
}
