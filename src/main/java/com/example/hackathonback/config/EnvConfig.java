package com.example.hackathonback.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // [한 줄 요약] .env 환경 변수를 로딩하기 위한 설정 클래스
// 이 클래스는 dotenv 라이브러리를 사용해 .env 파일의 값을 로딩하고,
// 해당 Dotenv 인스턴스를 Spring Bean으로 등록하여 전역에서 사용 가능하게 합니다.
public class EnvConfig {

    @Bean // [한 줄 요약] Dotenv 객체를 스프링 빈으로 등록
    public Dotenv dotenv() {
        return Dotenv.configure()
                .ignoreIfMalformed() // [한 줄 요약] 형식이 잘못된 항목은 무시하고 로드
                .ignoreIfMissing()   // [한 줄 요약] .env 파일이 없어도 무시 (예외 발생 안 함)
                .load();             // [한 줄 요약] 설정을 적용하고 .env 파일 로딩
    }
}
