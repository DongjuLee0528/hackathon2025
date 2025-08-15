package com.example.hackathonback.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Spring MVC 전역 설정 (인터셉터/리소스 핸들러)
@RequiredArgsConstructor // 생성자 주입 (필드 주입 제거)
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;       // 요청 로깅
    private final RateLimitingInterceptor rateLimitingInterceptor; // IP 기반 요청 제한

    /**
     * 요청 전 처리 로직을 수행할 인터셉터 등록
     * - Swagger 경로는 Rate Limiting 제외
     * - 실행 순서: RateLimiting(1) → Logging(2)
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
                )
                .order(1);

        registry.addInterceptor(loggingInterceptor)
                .order(2);
    }

    /**
     * Swagger UI 정적 리소스 경로 매핑
     * (Spring Boot 기본 자동 설정으로 충분한 경우 생략 가능)
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기존 springfox 호환
        registry.addResourceHandler("/swagger-ui.html**")
                .addResourceLocations("classpath:/META-INF/resources/");

        // webjars 리소스
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        // springdoc-ui 접근 보조 (환경에 따라 불필요할 수 있음)
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/");
    }
}
