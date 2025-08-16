package com.example.hackathonback.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    // 1 IP당 요청 간 최소 간격 3초
    private static final long LIMIT_INTERVAL_MS = 3000L;

    // IP별 마지막 요청 시간 저장
    private final Map<String, Instant> lastRequestTimes = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Swagger/문서 경로는 제외
        if (uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-resources") || uri.startsWith("/webjars")
                || uri.contains("favicon")) {
            return true;
        }

        String ip = request.getRemoteAddr();
        Instant now = Instant.now();
        Instant last = lastRequestTimes.getOrDefault(ip, Instant.EPOCH);

        // 이전 요청 이후 LIMIT_INTERVAL_MS 이내면 차단
        if (now.isBefore(last.plusMillis(LIMIT_INTERVAL_MS))) {
            long retryAfterSec = Math.max(1, (last.plusMillis(LIMIT_INTERVAL_MS).toEpochMilli() - now.toEpochMilli()) / 1000);
            response.setStatus(429); // Too Many Requests
            response.setHeader("Retry-After", String.valueOf(retryAfterSec));
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("요청이 너무 자주 발생했습니다. 잠시 후 다시 시도해주세요.");
            log.warn("Rate limited: ip={}, uri={}", ip, uri);
            return false;
        }

        // 요청 허용 → 마지막 요청 시간 갱신
        lastRequestTimes.put(ip, now);
        return true;
    }
}