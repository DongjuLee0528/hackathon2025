package com.example.hackathonback.review.controller;

import com.example.hackathonback.review.service.GptApiService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private static final int MAX_GPT_FILE_COUNT = 10;
    private static final int MAX_SINGLE_FILE_LENGTH = 10_000;
    private static final int MAX_MERGED_LENGTH = 20_000;
    private static final Set<String> ALLOWED_EXT = Set.of(".java", ".py", ".js");

    private final GptApiService gptApiService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
                return badRequest("파일이 없거나 파일 이름이 잘못되었습니다.");
            }

            String filename = file.getOriginalFilename().toLowerCase(Locale.ROOT);
            if (filename.endsWith(".zip")) {
                return handleZipFile(file);
            }

            if (!hasAllowedExtension(filename)) {
                return badRequest("지원하지 않는 파일 형식입니다. (.java, .py, .js 만 허용)");
            }

            String code = bytesToUtf8(file.getBytes());
            if (code.length() > MAX_SINGLE_FILE_LENGTH) {
                return badRequest("코드가 너무 깁니다. 단일 파일은 최대 " + MAX_SINGLE_FILE_LENGTH + "자까지 허용됩니다.");
            }

            return reviewCode(Collections.singletonList(code));

        } catch (IOException e) {
            return badRequest("파일 처리 중 오류가 발생했습니다.");
        } catch (Exception e) {
            return serverError("코드 리뷰 처리 중 오류가 발생했습니다.");
        }
    }

    @PostMapping(
            value = "/upload/text",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> uploadText(@RequestBody Map<String, String> request) {
        String code = request == null ? null : request.get("code");
        if (code == null || code.trim().isEmpty()) {
            return badRequest("코드가 비어있습니다.");
        }
        if (code.length() > MAX_SINGLE_FILE_LENGTH) {
            return badRequest("코드가 너무 깁니다. 단일 입력은 최대 " + MAX_SINGLE_FILE_LENGTH + "자까지 허용됩니다.");
        }
        return reviewCode(Collections.singletonList(code));
    }

    private ResponseEntity<Map<String, Object>> handleZipFile(MultipartFile zipFile) throws IOException {
        List<String> codeList = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;

                String name = entry.getName();
                if (name.contains("..") || name.startsWith("/") || name.startsWith("\\")) {
                    return badRequest("압축 파일에 잘못된 경로가 포함되어 있습니다.");
                }

                String lower = name.toLowerCase(Locale.ROOT);
                if (!hasAllowedExtension(lower)) continue;

                if (codeList.size() >= MAX_GPT_FILE_COUNT) {
                    return badRequest("파일이 너무 많습니다. 최대 " + MAX_GPT_FILE_COUNT + "개까지 업로드 가능합니다.");
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int len;
                int limit = 0;
                while ((len = zis.read(buffer)) > 0) {
                    if (limit + len > MAX_SINGLE_FILE_LENGTH * 4) {
                        return badRequest("개별 파일이 너무 큽니다. 파일: " + name);
                    }
                    baos.write(buffer, 0, len);
                    limit += len;
                }
                String code = new String(baos.toByteArray(), StandardCharsets.UTF_8);
                if (code.length() > MAX_SINGLE_FILE_LENGTH) {
                    return badRequest("개별 파일 코드가 너무 깁니다. 파일: " + name + ", 최대 " + MAX_SINGLE_FILE_LENGTH + "자");
                }
                codeList.add(code);
            }
        }

        if (codeList.isEmpty()) {
            return badRequest("압축 파일에 코드 파일(.java/.py/.js)이 없습니다.");
        }

        return reviewCode(codeList);
    }

    private ResponseEntity<Map<String, Object>> reviewCode(List<String> codes) {
        String mergedCode = String.join("\n\n/* ==== NEXT FILE ==== */\n\n", codes);
        if (mergedCode.length() > MAX_MERGED_LENGTH) {
            return badRequest("코드가 너무 깁니다. 전체는 최대 " + MAX_MERGED_LENGTH + "자까지 허용됩니다.");
        }

        try {
            gptApiService.requestCodeReview(mergedCode);
            return ok("코드 리뷰 요청 완료");
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage() == null ? "잘못된 요청입니다." : e.getMessage());
        } catch (Exception e) {
            return serverError("코드 리뷰 처리 중 오류가 발생했습니다.");
        }
    }

    private boolean hasAllowedExtension(@NotBlank String filenameLower) {
        for (String ext : ALLOWED_EXT) {
            if (filenameLower.endsWith(ext)) return true;
        }
        return false;
    }

    private String bytesToUtf8(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private ResponseEntity<Map<String, Object>> ok(String message) {
        return ResponseEntity.ok(Map.of("status", "success", "message", message));
    }

    private ResponseEntity<Map<String, Object>> badRequest(String message) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("status", "error", "message", message));
    }

    private ResponseEntity<Map<String, Object>> serverError(String message) {
        return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("status", "error", "message", message));
    }
}
