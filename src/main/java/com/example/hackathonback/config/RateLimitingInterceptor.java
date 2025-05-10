
package com.example.hackathonback.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private static final long LIMIT_INTERVAL_MS = 3000; // 3초 제한
    private final Map<String, Instant> lastRequestTimes = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        Instant now = Instant.now();
        Instant last = lastRequestTimes.getOrDefault(ip, Instant.EPOCH);

        if (now.isBefore(last.plusMillis(LIMIT_INTERVAL_MS))) {
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("요청이 너무 자주 발생했습니다. 잠시 후 다시 시도해주세요.");
            return false;
        }

        lastRequestTimes.put(ip, now);
        return true;
    }
}
