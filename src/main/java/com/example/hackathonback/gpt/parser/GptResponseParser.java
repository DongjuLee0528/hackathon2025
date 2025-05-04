package com.example.hackathonback.gpt.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * GPT 응답을 파싱하여 필요한 정보(요약, 문제점, 개선안 등)를 추출하는 유틸리티 클래스
 */
public class GptResponseParser {

    /**
     * GPT 응답 문자열을 파싱하여 구조화된 Map으로 반환
     *
     * @param gptResponse GPT에서 받아온 원시 응답 문자열
     * @return 각 항목(key)에 해당하는 값이 담긴 Map
     */
    public static Map<String, String> parse(String gptResponse) {
        Map<String, String> result = new HashMap<>();

        // TODO: 여기에 gptResponse를 실제로 파싱하는 로직을 구현해야 함
        // 현재는 테스트용 더미 데이터 사용

        result.put("summary", "요약 내용");                  // 요약 결과
        result.put("problem", "문제점 내용");                 // 문제점 설명
        result.put("suggestion", "개선 제안");                // 개선 방안
        result.put("beforeCode", "원본 코드");                // 원본 코드
        result.put("afterCode", "개선된 코드");               // 개선된 코드
        result.put("testability", "85");                      // 테스트 용이성 점수
        result.put("maintainability", "90");                  // 유지보수성 점수
        result.put("readability", "80");                      // 가독성 점수
        result.put("codeQuality", "88");                      // 코드 품질 점수
        result.put("structuralComplexity", "70");             // 구조적 복잡도 점수

        return result;
    }
}
