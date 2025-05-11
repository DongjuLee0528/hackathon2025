package com.example.hackathonback.common.file;

import java.io.File;

// [한 줄 요약] 파일 유효성 검사를 위한 유틸리티 클래스
// 이 클래스는 파일 존재 여부와 확장자 검사 등의 공통 파일 검증 로직을 제공합니다.
// 검증 실패 시 사용자 정의 예외인 FileHandlingException을 발생시킵니다.
public class FileValidator {

    /**
     * [한 줄 요약] 파일이 실제 존재하는지 확인
     *
     * 주어진 File 객체가 존재하지 않으면 FileHandlingException을 던집니다.
     * 주로 파일 경로 유효성 검사에 사용됩니다.
     *
     * @param file 존재 여부를 확인할 파일 객체
     */
    public static void validateExists(File file) {
        if (!file.exists()) {
            throw new FileHandlingException("해당 파일이 존재하지 않습니다: " + file.getPath());
        }
    }

    /**
     * [한 줄 요약] 파일 확장자가 허용된 목록에 포함되는지 검사
     *
     * 파일 이름이 지정된 허용 확장자 중 하나로 끝나는지 확인하고,
     * 해당하지 않으면 FileHandlingException을 발생시킵니다.
     * 확장자는 소문자 기준으로 비교합니다.
     *
     * @param filename 검사할 파일 이름
     * @param allowedExtensions 허용된 확장자 목록 (예: ".zip", ".txt")
     */
    public static void validateExtension(String filename, String... allowedExtensions) {
        for (String ext : allowedExtensions) {
            if (filename.toLowerCase().endsWith(ext)) {
                return;
            }
        }
        throw new FileHandlingException("허용되지 않은 파일 확장자입니다: " + filename);
    }
}
