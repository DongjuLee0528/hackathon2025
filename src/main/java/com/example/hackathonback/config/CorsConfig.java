package com.example.hackathonback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 프론트/백 도메인이 다를 때 CORS 허용.
 * - 쿠키 사용 시 allowCredentials(true) 필수
 * - Spring Security 에서 http.cors() 와 함께 사용
 */
@Configuration
public class CorsConfig {

    /**
     * 쉼표(,)로 구분된 허용 오리진 목록
     * 예) app.cors.allowed-origins=https://thecoder.djloghub.com,http://localhost:5173
     */
    @Value("${app.cors.allowed-origins:https://thecoder.djloghub.com,http://localhost:5173}")
    private String allowedOriginsProp;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedOrigins = Arrays.stream(allowedOriginsProp.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowCredentials(true); // 쿠키/인증 정보 허용
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Set-Cookie")); // 클라이언트에서 읽을 헤더
        config.setMaxAge(3600L); // preflight 캐시(초)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
