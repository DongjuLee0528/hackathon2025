package com.example.hackathonback.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${app.frontend.failure-redirect}")
    private String frontendFailureRedirect;

    @Value("${app.debug:false}")
    private boolean appDebug;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String reason = mapReason(exception, request);
        String message = mapMessage(exception);

        log.warn("OAuth2 로그인 실패: reason={}, message={}, ex={}",
                reason, message, exception.getClass().getSimpleName());

        StringBuilder target = new StringBuilder(frontendFailureRedirect);
        if (!frontendFailureRedirect.contains("?")) target.append("?");
        else target.append("&");

        target.append("reason=").append(encode(reason));

        if (appDebug && message != null && !message.isBlank()) {
            target.append("&debug=true");
            target.append("&message=").append(encode(truncate(message, 300)));
        }

        response.sendRedirect(target.toString());
    }

    private String mapReason(AuthenticationException ex, HttpServletRequest req) {
        String providerError = req.getParameter("error");
        if ("access_denied".equalsIgnoreCase(providerError)) return "access_denied";

        if (ex instanceof OAuth2AuthenticationException oae) {
            String errorCode = oae.getError() != null ? oae.getError().getErrorCode() : null;
            if (errorCode != null) {
                String ec = errorCode.toLowerCase();
                if (ec.contains("invalid_state")) return "invalid_state";
                if (ec.contains("access_denied")) return "access_denied";
                if (ec.contains("temporarily_unavailable") || ec.contains("server_error"))
                    return "provider_error";
            }
            return "token_exchange_failed";
        }

        String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        if (msg.contains("connection") || msg.contains("timed out")) return "network_error";
        if (msg.contains("redirect_uri") || msg.contains("mismatch")) return "configuration_error";

        return "auth_failed";
    }

    private String mapMessage(AuthenticationException ex) {
        String msg = ex.getMessage();
        if (msg == null) return null;
        return msg.replaceAll("\\s+", " ").trim();
    }

    private String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }
}
