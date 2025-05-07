package com.example.hackathonback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 프로젝트의 시작점이 되는 Spring Boot 애플리케이션 클래스
 */
@SpringBootApplication // 컴포넌트 스캔, 자동 설정 등을 포함한 애플리케이션 설정
public class HackathonBackApplication {

    /**
     * 애플리케이션의 메인 메서드 (프로그램 실행 시작점)
     * @param args 커맨드 라인 인자
     */
    public static void main(String[] args) {
        // Spring Boot 애플리케이션 실행
        SpringApplication.run(HackathonBackApplication.class, args);
    }
}
