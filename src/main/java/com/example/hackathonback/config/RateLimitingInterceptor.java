package com.example.hackathonback.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    // 요청 간 최소 간격(밀리초) — 필요시 properties로 빼서 튜닝
    private static final long LIMIT_INTERVAL_MS = 1000L;

    // 화이트리스트(레이트리밋 미적용)
    private static final Set<String> WHITELIST_PATHS = Set.of(
            "/user",                         // 로그인 직후 사용자 정보 확인
            "/oauth/success",                // 리다이렉트 완료 페이지
            "/login/oauth2/code/github"      // OAuth 콜백
    );

    // IP+URI별 마지막 요청 시간
    private final Map<String, Instant> lastRequestTimes = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String method = request.getMethod();
        final String uri = request.getRequestURI();

        // 1) CORS 프리플라이트는 무조건 통과
        if ("OPTIONS".equalsIgnoreCase(method)) return true;

        // 2) Swagger/문서/정적 리소스/헬스체크 제외
        if (uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-resources") || uri.startsWith("/webjars")
                || uri.contains("favicon")
                || uri.startsWith("/actuator")
                || uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg") || uri.endsWith(".svg")) {
            return true;
        }

        // 3) 화이트리스트 경로는 통과
        if (WHITELIST_PATHS.contains(uri)) return true;

        // 4) IP 추출 (프록시/Nginx 뒤에서는 X-Forwarded-For 사용)
        final String ip = getClientIp(request);

        // 5) IP+URI 기준으로 제한
        final String key = ip + "|" + uri;
        Instant now = Instant.now();
        Instant last = lastRequestTimes.getOrDefault(key, Instant.EPOCH);

        if (now.isBefore(last.plusMillis(LIMIT_INTERVAL_MS))) {
            long retryAfterSec = Math.max(1, (last.plusMillis(LIMIT_INTERVAL_MS).toEpochMilli() - now.toEpochMilli()) / 1000);
            response.setStatus(429); // Too Many Requests
            response.setHeader("Retry-After", String.valueOf(retryAfterSec));
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("요청이 너무 자주 발생했습니다. 잠시 후 다시 시도해주세요.");
            log.warn("Rate limited: ip={}, uri={}", ip, uri);
            return false;
        }

        lastRequestTimes.put(key, now);
        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            // 가장 앞의 클라이언트 IP 사용
            int comma = xff.indexOf(',');
            return (comma > 0 ? xff.substring(0, comma) : xff).trim();
        }
        return request.getRemoteAddr();
    }
}
