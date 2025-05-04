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
     * 원본 문자열과 수정된 문자열을 비교하여 라인 단위 diff를 생성
     *
     * @param original 원본 코드/문자열
     * @param modified 수정된 코드/문자열
     * @return 라인별 diff 결과 (문자열)
     */
    public static String generateDiff(String original, String modified) {
        String[] originalLines = original.split("\\r?\\n"); // 원본 문자열을 줄 단위로 분리
        String[] modifiedLines = modified.split("\\r?\\n"); // 수정된 문자열을 줄 단위로 분리

        List<String> diffResult = new ArrayList<>();

        int maxLength = Math.max(originalLines.length, modifiedLines.length); // 더 긴 쪽 기준으로 비교

        for (int i = 0; i < maxLength; i++) {
            String originalLine = i < originalLines.length ? originalLines[i] : "";
            String modifiedLine = i < modifiedLines.length ? modifiedLines[i] : "";

            if (!originalLine.equals(modifiedLine)) {
                diffResult.add("- " + originalLine); // 삭제된 라인
                diffResult.add("+ " + modifiedLine); // 추가된 라인
            } else {
                diffResult.add("  " + originalLine); // 변경되지 않은 라인
            }
        }

        return String.join("\n", diffResult); // diff 결과를 문자열로 반환
    }
}
