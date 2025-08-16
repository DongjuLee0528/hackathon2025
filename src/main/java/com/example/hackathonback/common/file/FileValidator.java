package com.example.hackathonback.common.file;

import java.io.File;

/**
 * 파일 유효성 검사용 유틸리티.
 * - 존재/읽기 가능/확장자 화이트리스트 검사
 * - 실패 시 FileHandlingException 발생
 */
public class FileValidator {

    /** 파일 존재 + 일반 파일 + 읽기 가능 여부 검사 */
    public static void validateExists(File file) {
        if (file == null) {
            throw new FileHandlingException("파일이 null 입니다.");
        }
        if (!file.exists()) {
            throw new FileHandlingException("해당 파일이 존재하지 않습니다: " + file.getPath());
        }
        if (!file.isFile()) {
            throw new FileHandlingException("디렉터리는 허용되지 않습니다: " + file.getPath());
        }
        if (!file.canRead()) {
            throw new FileHandlingException("파일을 읽을 수 없습니다: " + file.getPath());
        }
    }

    /**
     * 파일명 확장자 화이트리스트 검사(대소문자 무시).
     * 허용 확장자는 ".zip" / "zip" 모두 허용 형태로 입력 가능.
     */
    public static void validateExtension(String filename, String... allowedExtensions) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new FileHandlingException("파일명이 비어 있습니다.");
        }
        if (allowedExtensions == null || allowedExtensions.length == 0) {
            throw new FileHandlingException("허용 확장자 목록이 비어 있습니다.");
        }

        String lower = filename.toLowerCase().trim();
        String actualExt = extractExt(lower); // ".txt" 형태 또는 빈 문자열

        for (String ext : allowedExtensions) {
            if (ext == null || ext.trim().isEmpty()) continue;
            String normalized = normalizeExt(ext);
            if (normalized.equals(actualExt)) {
                return; // 통과
            }
        }
        throw new FileHandlingException("허용되지 않은 파일 확장자입니다: " + filename);
    }

    /** ".txt" 형태로 통일 */
    private static String normalizeExt(String ext) {
        String e = ext.trim().toLowerCase();
        return e.startsWith(".") ? e : "." + e;
    }

    /** 파일명에서 마지막 점(.) 기준 확장자 추출, 없으면 빈 문자열 */
    private static String extractExt(String name) {
        int idx = name.lastIndexOf('.');
        return (idx >= 0 && idx < name.length() - 1) ? name.substring(idx).toLowerCase() : "";
    }
}
