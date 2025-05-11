package com.example.hackathonback;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HackathonBackApplication {

    public static void main(String[] args) {
        // .env 로딩
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        // Spring이 인식할 수 있도록 시스템 속성으로 설정
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

        SpringApplication.run(HackathonBackApplication.class, args);
    }

    private static void setIfExists(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        if (value != null) {
            System.setProperty(key, value);
        }
    }
}
