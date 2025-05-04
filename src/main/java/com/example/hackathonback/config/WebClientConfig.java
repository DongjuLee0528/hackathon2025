package com.example.hackathonback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration // 스프링 설정 클래스임을 나타냄
public class WebClientConfig {

    /**
     * WebClient.Builder를 빈으로 등록
     * - WebClient는 비동기/반응형 HTTP 요청을 보낼 때 사용
     * - 필요한 곳에서 의존성 주입(@Autowired 등)으로 사용 가능
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
