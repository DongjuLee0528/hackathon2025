package com.example.hackathonback.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component // [한 줄 요약] IP 기반 요청 속도 제한(rate limiting)을 수행하는 인터셉터
// 이 클래스는 동일 IP로부터의 과도한 요청을 제한하여 서버 과부하 또는 abuse를 방지합니다.
public class RateLimitingInterceptor implements HandlerInterceptor {

    private static final long LIMIT_INTERVAL_MS = 3000; // [한 줄 요약] 1 IP당 요청 간 최소 간격 3초
    private final Map<String, Instant> lastRequestTimes = new ConcurrentHashMap<>(); // [한 줄 요약] IP별 마지막 요청 시간 저장

    /**
     * [한 줄 요약] 컨트롤러 진입 전 요청 속도를 제한
     *
     * 동일 IP에서 마지막 요청 이후 3초가 지나지 않았다면 429 응답을 반환하여 요청을 차단합니다.
     * 단, Swagger 및 문서 관련 URI는 제한 없이 허용합니다.
     *
     * @param request  클라이언트 요청
     * @param response 서버 응답
     * @param handler  실행될 컨트롤러 핸들러
     * @return 요청을 계속 진행할 수 있으면 true, 차단 시 false
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // [한 줄 요약] Swagger 및 문서 요청은 rate limit 대상에서 제외
        if (uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-resources") || uri.startsWith("/webjars")
                || uri.contains("favicon")) {
            return true;
        }

        String ip = request.getRemoteAddr(); // [한 줄 요약] 클라이언트 IP 주소 가져오기
        Instant now = Instant.now();
        Instant last = lastRequestTimes.getOrDefault(ip, Instant.EPOCH); // 이전 요청 시간 (기본값은 옛날 시간)

        // [한 줄 요약] 이전 요청 시간으로부터 3초가 지나지 않았으면 차단
        if (now.isBefore(last.plusMillis(LIMIT_INTERVAL_MS))) {
            response.setStatus(429); // HTTP 상태 코드: Too Many Requests
            response.getWriter().write("요청이 너무 자주 발생했습니다. 잠시 후 다시 시도해주세요.");
            return false;
        }

        // [한 줄 요약] 요청 허용 → 현재 시각을 마지막 요청 시간으로 갱신
        lastRequestTimes.put(ip, now);
        return true;
    }
}
