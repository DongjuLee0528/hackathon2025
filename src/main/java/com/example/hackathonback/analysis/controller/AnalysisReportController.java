package com.example.hackathonback.analysis.controller;

import com.example.hackathonback.analysis.entity.AnalysisReport;
import com.example.hackathonback.analysis.service.AnalysisReportService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스는 REST API의 컨트롤러임을 나타냄
@RequestMapping("/api/reports") // 이 컨트롤러의 기본 URL 경로 설정
public class AnalysisReportController {

    private final AnalysisReportService service;

    // 생성자를 통해 AnalysisReportService 주입
    public AnalysisReportController(AnalysisReportService service) {
        this.service = service;
    }

    /**
     * 모든 분석 리포트를 조회하는 GET 요청 핸들러
     * GET /api/reports
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnalysisReport>> getAllReports() {
        List<AnalysisReport> reports = service.findAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 특정 ID의 분석 리포트를 조회하는 GET 요청 핸들러
     * GET /api/reports/{id}
     * @param id 조회할 리포트의 ID
     * @return 해당 ID의 리포트가 존재하면 OK(200)와 함께 반환, 없으면 404 Not Found
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnalysisReport> getReportById(@PathVariable @Positive Long id) {
        return service.findReportById(id)
                .map(ResponseEntity::ok) // 리포트가 존재하면 200 OK 반환
                .orElse(ResponseEntity.notFound().build()); // 존재하지 않으면 404 Not Found 반환
    }
}
