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
 * 코드 리뷰를 위한 파일 업로드 및 텍스트 입력을 처리하는 컨트롤러 클래스입니다.
 * - 파일 업로드 (단일 파일 또는 zip 파일)
 * - 텍스트 입력
 * - zip 파일 압축 해제 및 코드 파일 추출
 * - 코드 파일 병합 및 코드 길이 초과 체크
 * - GPT 리뷰 요청
 */
@RestController
@RequestMapping("/review")
public class ReviewController {

    private static final int MAX_GPT_FILE_COUNT = 10; // GPT에 보낼 수 있는 최대 파일 개수
    private static final int MAX_CODE_LENGTH = 20000; // 코드 최대 길이 제한 (문자 수)

    private final GptApiService gptApiService;

    public ReviewController(GptApiService gptApiService) {
        this.gptApiService = gptApiService;
    }

    /**
     * 파일 업로드를 처리하는 메소드입니다.
     * 단일 코드 파일 또는 zip 파일 업로드를 지원합니다.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            return ResponseEntity.badRequest().body("파일이 없거나 파일 이름이 잘못되었습니다.");
        }

        String filename = file.getOriginalFilename();

        if (filename.endsWith(".zip")) {
            return handleZipFile(file);
        } else {
            String code = new String(file.getBytes(), StandardCharsets.UTF_8);
            return reviewCode(Collections.singletonList(code));
        }
    }

    /**
     * 텍스트 입력을 통한 코드 리뷰 요청을 처리하는 메소드입니다.
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
     * zip 파일을 처리하는 메소드입니다.
     */
    private ResponseEntity<String> handleZipFile(MultipartFile zipFile) throws IOException {
        List<String> codeList = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;

                if (entry.getName().contains("..") || entry.getName().startsWith("/")) {
                    return ResponseEntity.badRequest().body("압축 파일에 잘못된 경로가 포함되어 있습니다.");
                }

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
     * 코드 리스트를 검토하여 GPT에 리뷰 요청을 보냅니다.
     */
    private ResponseEntity<String> reviewCode(List<String> codes) {
        String mergedCode;

        if (codes.size() > MAX_GPT_FILE_COUNT) {
            mergedCode = String.join("\n\n", codes);
        } else {
            mergedCode = String.join("\n\n", codes);
        }

        if (mergedCode.length() > MAX_CODE_LENGTH) {
            return ResponseEntity.badRequest().body("에러: 코드가 너무 깁니다. 파일 수를 줄여서 다시 업로드해주세요.");
        }

        gptApiService.requestCodeReview(mergedCode);

        return ResponseEntity.ok("코드 리뷰 요청 완료");
    }
}
