package com.example.hackathonback;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // [한 줄 요약] 스프링 부트 애플리케이션 진입점 (자동 설정 + 컴포넌트 스캔)
public class HackathonBackApplication {

    public static void main(String[] args) {
        // [한 줄 요약] .env 파일 로드 (환경 변수 설정용)
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed() // [한 줄 요약] 형식 오류 무시
                .ignoreIfMissing()   // [한 줄 요약] .env 파일 없을 경우 무시
                .load();

        // [한 줄 요약] 필요한 환경 변수들을 시스템 속성으로 등록
        setIfExists(dotenv, "DB_URL");
        setIfExists(dotenv, "DB_USERNAME");
        setIfExists(dotenv, "DB_PASSWORD");

        setIfExists(dotenv, "GITHUB_CLIENT_SECRET");
        setIfExists(dotenv, "GITLAB_CLIENT_SECRET");

        setIfExists(dotenv, "GPT_PROBLEM_KEY");
        setIfExists(dotenv, "GPT_JUDGE_KEY");
        setIfExists(dotenv, "GPT_REVIEW_KEY");
        setIfExists(dotenv, "GPT_FEEDBACK_KEY");

        setIfExists(dotenv, "GPT_PROBLEM_MODEL");
        setIfExists(dotenv, "GPT_JUDGE_MODEL");
        setIfExists(dotenv, "GPT_REVIEW_MODEL");
        setIfExists(dotenv, "GPT_FEEDBACK_MODEL");

        SpringApplication.run(HackathonBackApplication.class, args); // [한 줄 요약] 스프링 애플리케이션 실행
    }

    /**
     * [한 줄 요약] .env에서 값을 가져와 존재할 경우 시스템 속성으로 등록
     *
     * @param dotenv 환경 변수 로더
     * @param key .env 키 이름
     */
    private static void setIfExists(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        if (value != null) {
            System.setProperty(key, value); // 스프링이 인식할 수 있도록 설정
        }
    }
}
