package com.example.hackathonback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration // [한 줄 요약] WebClient 설정을 위한 스프링 설정 클래스
// 이 클래스는 WebClient.Builder를 전역에서 사용 가능하도록 빈으로 등록합니다.
public class WebClientConfig {

    /**
     * [한 줄 요약] WebClient.Builder를 Bean으로 등록
     *
     * WebClient는 Spring WebFlux에서 제공하는 비동기/논블로킹 HTTP 클라이언트입니다.
     * 외부 API(GitHub, GPT 등) 호출 시 사용되며, 이 빌더를 통해 커스터마이징이 가능합니다.
     * 필요 시 `.baseUrl()`, `.defaultHeader()` 등을 설정할 수 있습니다.
     *
     * 이 메서드로 생성된 WebClient.Builder는 서비스 클래스 등에서 의존성 주입(@Autowired, 생성자 등)으로 사용됩니다.
     *
     * @return WebClient.Builder 객체
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
