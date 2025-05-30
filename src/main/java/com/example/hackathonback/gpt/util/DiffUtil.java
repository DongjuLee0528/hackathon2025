package com.example.hackathonback.gpt.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 두 문자열(original, modified)을 비교하여 라인 단위로 차이를 출력하는 유틸리티 클래스
 * - '-'로 시작하면 삭제된 라인
 * - '+'로 시작하면 추가된 라인
 * - 공백으로 시작하면 변경되지 않은 라인
 */
public class DiffUtil {

    /**
     * 원본 문자열과 수정된 문자열을 비교하여 라인 단위로 diff 결과를 생성합니다.
     *
     * @param original 원본 코드 또는 문자열
     * @param modified 수정된 코드 또는 문자열
     * @return 변경 사항을 라인별로 표시한 문자열 (삭제: '-', 추가: '+', 동일: ' ')
     */
    public static String generateDiff(String original, String modified) {
        // 입력된 문자열을 줄 단위로 분리
        String[] originalLines = original.split("\\r?\\n");
        String[] modifiedLines = modified.split("\\r?\\n");

        // 최종 diff 결과를 담을 리스트
        List<String> diffResult = new ArrayList<>();

        // 두 배열 중 더 긴 길이를 기준으로 반복
        int maxLength = Math.max(originalLines.length, modifiedLines.length);

        for (int i = 0; i < maxLength; i++) {
            // 원본 라인이 존재하지 않으면 빈 문자열로 대체
            String originalLine = i < originalLines.length ? originalLines[i] : "";
            // 수정된 라인이 존재하지 않으면 빈 문자열로 대체
            String modifiedLine = i < modifiedLines.length ? modifiedLines[i] : "";

            // 두 라인이 서로 다르면 삭제 및 추가 표시
            if (!originalLine.equals(modifiedLine)) {
                diffResult.add("- " + originalLine); // 삭제된 라인
                diffResult.add("+ " + modifiedLine); // 추가된 라인
            } else {
                diffResult.add("  " + originalLine); // 동일한 라인은 공백 접두사
            }
        }

        // 리스트를 개행 문자로 연결하여 최종 diff 문자열 반환
        return String.join("\n", diffResult);
    }
}
