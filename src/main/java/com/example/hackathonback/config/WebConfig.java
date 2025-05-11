package com.example.hackathonback.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // [한 줄 요약] Spring MVC 전역 설정 클래스 (인터셉터 및 리소스 핸들러 등록)
// 이 클래스는 인터셉터(Rate Limiting, Logging) 등록과 Swagger 정적 리소스 경로 설정을 담당합니다.
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor; // [한 줄 요약] 요청 로그 기록 인터셉터

    @Autowired
    private RateLimitingInterceptor rateLimitingInterceptor; // [한 줄 요약] IP 기반 요청 제한 인터셉터

    /**
     * [한 줄 요약] 요청 전 처리 로직을 수행할 인터셉터들을 등록
     *
     * - rateLimitingInterceptor는 Swagger 관련 경로는 제외하고 모든 요청에 적용됩니다.
     * - loggingInterceptor는 모든 요청에 대해 적용되어 요청 로그를 출력합니다.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor)
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/favicon.ico"
                ); // [한 줄 요약] Swagger 문서 관련 경로는 Rate Limiting 제외

        registry.addInterceptor(loggingInterceptor); // [한 줄 요약] 모든 요청에 대해 로그 기록 수행
    }

    /**
     * [한 줄 요약] Swagger UI 관련 정적 리소스 경로 매핑
     *
     * Swagger UI 페이지 및 관련 자원(webjars)을 classpath 경로에서 서빙할 수 있도록 설정합니다.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html**")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
