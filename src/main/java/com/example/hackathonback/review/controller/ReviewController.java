package com.example.hackathonback.review.controller;

import com.example.hackathonback.review.service.GptApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 코드 리뷰를 위한 파일 업로드 및 텍스트 입력을 처리하는 컨트롤러
 * - 단일 코드 파일, zip 파일 업로드
 * - 텍스트 직접 입력
 * - zip 파일 압축 해제 및 코드 파일 추출 (.java, .py, .js)
 * - 파일 병합 및 길이 제한 확인 후 GPT 리뷰 요청
 */
@RestController
@RequestMapping("/review")
public class ReviewController {

    private static final int MAX_GPT_FILE_COUNT = 10;     // 최대 허용 파일 수
    private static final int MAX_CODE_LENGTH = 20000;     // GPT 요청 시 최대 코드 길이

    private final GptApiService gptApiService;

    public ReviewController(GptApiService gptApiService) {
        this.gptApiService = gptApiService;
    }

    /**
     * 파일 업로드 리뷰 요청 (단일 파일 또는 zip)
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            return ResponseEntity.badRequest().body("파일이 없거나 파일 이름이 잘못되었습니다.");
        }

        String filename = file.getOriginalFilename();

        // zip 파일인 경우 zip 처리
        if (filename.endsWith(".zip")) {
            return handleZipFile(file);
        } else {
            // 단일 코드 파일 처리
            String code = new String(file.getBytes(), StandardCharsets.UTF_8);
            return reviewCode(Collections.singletonList(code));
        }
    }

    /**
     * 텍스트 입력을 통한 리뷰 요청
     */
    @PostMapping("/upload/text")
    public ResponseEntity<String> uploadText(@RequestBody Map<String, String> request) {
        String code = request.get("code");

        if (code == null || code.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("코드가 비어있습니다.");
        }

        return reviewCode(Collections.singletonList(code));
    }

    /**
     * zip 파일 내 코드 파일 추출 및 리뷰 요청 처리
     */
    private ResponseEntity<String> handleZipFile(MultipartFile zipFile) throws IOException {
        List<String> codeList = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;

                // 보안: 상대경로 탈출 방지
                if (entry.getName().contains("..") || entry.getName().startsWith("/")) {
                    return ResponseEntity.badRequest().body("압축 파일에 잘못된 경로가 포함되어 있습니다.");
                }

                // 허용된 코드 파일 확장자만 처리
                if (entry.getName().endsWith(".java") || entry.getName().endsWith(".py") || entry.getName().endsWith(".js")) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }
                    String code = baos.toString(StandardCharsets.UTF_8);
                    codeList.add(code);
                }
            }
        }

        if (codeList.isEmpty()) {
            return ResponseEntity.badRequest().body("압축 파일에 코드 파일이 없습니다.");
        }

        return reviewCode(codeList);
    }

    /**
     * 코드 리스트를 병합하고 GPT에 리뷰 요청 전송
     */
    private ResponseEntity<String> reviewCode(List<String> codes) {
        // 여러 파일 병합
        String mergedCode = String.join("\n\n", codes);

        // 코드 길이 초과 시 에러 반환
        if (mergedCode.length() > MAX_CODE_LENGTH) {
            return ResponseEntity.badRequest().body("에러: 코드가 너무 깁니다. 파일 수를 줄여서 다시 업로드해주세요.");
        }

        // GPT 코드 리뷰 요청
        gptApiService.requestCodeReview(mergedCode);

        return ResponseEntity.ok("코드 리뷰 요청 완료");
    }
}
