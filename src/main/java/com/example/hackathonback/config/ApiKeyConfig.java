package com.example.hackathonback.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component // [한 줄 요약] .env 파일의 API 키 값을 가져오는 Spring Bean 클래스
// 이 클래스는 Dotenv 라이브러리를 사용하여 .env 파일의 GPT 및 GitHub 관련 API 키를 읽어옵니다.
// 스프링 컴포넌트로 등록되어 의존성 주입을 통해 다른 클래스에서 사용 가능합니다.
public class ApiKeyConfig {

    private final Dotenv dotenv; // [한 줄 요약] 환경 변수(.env)를 읽어들이는 객체

    // [한 줄 요약] 생성자를 통해 Dotenv 의존성 주입
    public ApiKeyConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    // 아래 메서드들은 각각 .env 파일에서 설정한 키 값을 반환합니다.
    // 키 이름은 정확히 .env에 정의된 이름과 일치해야 하며, 값이 없을 경우 null이 반환됩니다.

    public String getProblemKey() {
        return dotenv.get("GPT_PROBLEM_KEY"); // [한 줄 요약] 문제 생성용 GPT API 키
    }

    public String getProblemModel() {
        return dotenv.get("GPT_PROBLEM_MODEL"); // [한 줄 요약] 문제 생성에 사용되는 GPT 모델명
    }

    public String getJudgeKey() {
        return dotenv.get("GPT_JUDGE_KEY"); // [한 줄 요약] 채점용 GPT API 키
    }

    public String getJudgeModel() {
        return dotenv.get("GPT_JUDGE_MODEL"); // [한 줄 요약] 채점에 사용되는 GPT 모델명
    }

    public String getReviewKey() {
        return dotenv.get("GPT_REVIEW_KEY"); // [한 줄 요약] 코드 리뷰용 GPT API 키
    }

    public String getReviewModel() {
        return dotenv.get("GPT_REVIEW_MODEL"); // [한 줄 요약] 코드 리뷰에 사용되는 GPT 모델명
    }

    public String getFeedbackKey() {
        return dotenv.get("GPT_FEEDBACK_KEY"); // [한 줄 요약] 피드백용 GPT API 키
    }

    public String getFeedbackModel() {
        return dotenv.get("GPT_FEEDBACK_MODEL"); // [한 줄 요약] 피드백 생성에 사용되는 GPT 모델명
    }

    public String getGithubClientSecret() {
        return dotenv.get("GITHUB_CLIENT_SECRET"); // [한 줄 요약] GitHub OAuth 클라이언트 시크릿
    }
}
