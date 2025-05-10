
package com.example.hackathonback.common.file;

import java.io.File;

public class FileValidator {

    public static void validateExists(File file) {
        if (!file.exists()) {
            throw new FileHandlingException("해당 파일이 존재하지 않습니다: " + file.getPath());
        }
    }

    public static void validateExtension(String filename, String... allowedExtensions) {
        for (String ext : allowedExtensions) {
            if (filename.toLowerCase().endsWith(ext)) {
                return;
            }
        }
        throw new FileHandlingException("허용되지 않은 파일 확장자입니다: " + filename);
    }
}
