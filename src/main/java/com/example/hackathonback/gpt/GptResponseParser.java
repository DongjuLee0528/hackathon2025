package com.example.hackathonback.gpt;

import java.util.HashMap;
import java.util.Map;

public class GptResponseParser {

    public static Map<String, String> parse(String gptResponse) {
        Map<String, String> result = new HashMap<>();
        // 여기서는 gptResponse를 파싱하는 로직이 들어가야 함
        // 예시로 dummy 데이터 삽입
        result.put("summary", "요약 내용");
        result.put("problem", "문제점 내용");
        result.put("suggestion", "개선 제안");
        result.put("beforeCode", "원본 코드");
        result.put("afterCode", "개선된 코드");
        result.put("testability", "85");
        result.put("maintainability", "90");
        result.put("readability", "80");
        result.put("codeQuality", "88");
        result.put("structuralComplexity", "70");
        return result;
    }
}