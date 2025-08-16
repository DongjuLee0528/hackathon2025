package com.example.hackathonback.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 예: [REQUEST] GET /api/github/fetch/... (ip: 203.0.113.10)
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        log.info("[REQUEST] {} {} (ip: {})", method, uri, ip);
        return true;
    }
}
