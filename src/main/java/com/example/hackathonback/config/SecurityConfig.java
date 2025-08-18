package com.example.hackathonback.config;

import com.example.hackathonback.security.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Value("${app.frontend.base}")
    private String frontendBase; // 예: https://thecoder.djloghub.com

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // ✅ CORS는 CorsConfig의 빈 사용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**", "/v3/api-docs/**",
                                "/health", "/actuator/**",
                                "/", "/index.html",
                                "/favicon.ico", "/assets/**",
                                "/oauth2/**", "/login/**",
                                "/user" // 프론트 초기 세션확인용 허용
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/oauth2/authorization/github")
                        .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            String target = frontendBase.endsWith("/")
                                    ? frontendBase + "oauth/callback"
                                    : frontendBase + "/oauth/callback";
                            response.sendRedirect(target);
                        })
                        .failureHandler(oAuth2FailureHandler)
                )
                .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }
}
