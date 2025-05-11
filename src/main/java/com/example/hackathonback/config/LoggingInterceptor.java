package com.example.hackathonback.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component // [한 줄 요약] 요청 정보를 로그로 출력하는 인터셉터 클래스
// 이 클래스는 Spring MVC의 인터셉터로 등록되어, 컨트롤러에 요청이 들어오기 전 로그를 출력합니다.
// 주로 디버깅이나 요청 추적, 모니터링 목적에 사용됩니다.
public class LoggingInterceptor implements HandlerInterceptor {

    /**
     * [한 줄 요약] 컨트롤러 실행 전에 요청 메서드 및 URI를 로그로 출력
     *
     * 이 메서드는 요청이 컨트롤러로 전달되기 전에 실행되며,
     * 요청 방식(GET, POST 등)과 요청 URI를 콘솔에 출력합니다.
     *
     * @param request  클라이언트의 HTTP 요청
     * @param response 서버의 HTTP 응답
     * @param handler  실제 호출될 컨트롤러 핸들러 객체
     * @return true를 반환하면 요청 처리를 계속 진행하고, false면 중단됨
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("[REQUEST] " + request.getMethod() + " " + request.getRequestURI()); // 예: [REQUEST] GET /api/github/fetch/...
        return true; // true를 반환하여 다음 필터나 컨트롤러로 요청 전달 계속
    }
}
