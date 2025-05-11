package com.example.hackathonback.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // [한 줄 요약] Swagger(OpenAPI 3) 설정 클래스
// 이 클래스는 SpringDoc(OpenAPI 3 기반 Swagger 라이브러리)을 사용하여 API 문서를 자동 생성합니다.
public class SwaggerConfig {

    /**
     * [한 줄 요약] 전체 API 문서의 기본 정보 설정
     *
     * Swagger UI에 표시될 API 문서의 제목, 설명, 버전 등을 설정합니다.
     *
     * @return OpenAPI 객체
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hackathon API 문서") // 문서 제목
                        .description("이 문서는 Swagger(OpenAPI 3)로 자동 생성된 문서입니다.") // 문서 설명
                        .version("v1.0.0")); // 문서 버전
    }

    /**
     * [한 줄 요약] 공개 API 그룹 설정
     *
     * 이 설정을 통해 특정 경로 패턴을 가진 API들을 그룹으로 묶어 Swagger UI에서 구분해서 보여줄 수 있습니다.
     * 현재는 모든 경로(`/`)를 포함하도록 설정되어 있습니다.
     *
     * @return GroupedOpenApi 객체
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1-definition") // Swagger UI에서 표시될 그룹명
                .pathsToMatch("/**")   // 모든 경로를 문서화 대상으로 지정
                .build();
    }
}
