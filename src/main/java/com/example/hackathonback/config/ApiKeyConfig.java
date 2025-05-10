package com.example.hackathonback.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyConfig {

    private final Dotenv dotenv;

    public ApiKeyConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    public String getProblemKey() {
        return dotenv.get("GPT_PROBLEM_KEY");
    }

    public String getProblemModel() {
        return dotenv.get("GPT_PROBLEM_MODEL");
    }

    public String getJudgeKey() {
        return dotenv.get("GPT_JUDGE_KEY");
    }

    public String getJudgeModel() {
        return dotenv.get("GPT_JUDGE_MODEL");
    }

    public String getReviewKey() {
        return dotenv.get("GPT_REVIEW_KEY");
    }

    public String getReviewModel() {
        return dotenv.get("GPT_REVIEW_MODEL");
    }

    public String getFeedbackKey() {
        return dotenv.get("GPT_FEEDBACK_KEY");
    }

    public String getFeedbackModel() {
        return dotenv.get("GPT_FEEDBACK_MODEL");
    }

    public String getGithubClientSecret() {
        return dotenv.get("GITHUB_CLIENT_SECRET");
    }
}
