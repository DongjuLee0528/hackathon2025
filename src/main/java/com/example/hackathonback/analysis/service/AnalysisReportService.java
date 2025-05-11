package com.example.hackathonback.analysis.service;

import com.example.hackathonback.analysis.entity.AnalysisReport;
import com.example.hackathonback.analysis.repository.AnalysisReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // [한 줄 요약] 분석 리포트에 대한 비즈니스 로직을 처리하는 서비스 클래스
// 이 클래스는 PR 분석 결과를 저장하고 조회하는 로직을 담당하며, 컨트롤러와 리포지토리 사이에서 동작합니다.
public class AnalysisReportService {

    private final AnalysisReportRepository repository; // [한 줄 요약] DB 접근을 위한 JPA 리포지토리 의존성

    // [한 줄 요약] 생성자 주입을 통해 리포지토리 초기화
    // 스프링이 자동으로 AnalysisReportRepository를 주입해줍니다.
    public AnalysisReportService(AnalysisReportRepository repository) {
        this.repository = repository;
    }

    /**
     * [한 줄 요약] 새로운 분석 리포트를 생성하고 DB에 저장
     *
     * 주어진 PR URL과 분석 결과를 기반으로 AnalysisReport 객체를 생성한 후,
     * JPA를 통해 데이터베이스에 저장합니다.
     *
     * @param prUrl 분석 대상 PR(Pull Request)의 URL
     * @param result 코드 분석 결과 텍스트
     * @return 저장된 AnalysisReport 객체
     */
    public AnalysisReport saveReport(String prUrl, String result) {
        AnalysisReport report = new AnalysisReport();
        report.setPrUrl(prUrl);
        report.setAnalysisResult(result);
        return repository.save(report); // [한 줄 요약] DB에 엔티티 저장
    }

    /**
     * [한 줄 요약] 모든 분석 리포트 조회
     *
     * DB에 저장된 모든 AnalysisReport 레코드를 리스트 형태로 반환합니다.
     *
     * @return 분석 리포트 리스트
     */
    public List<AnalysisReport> findAllReports() {
        return repository.findAll();
    }

    /**
     * [한 줄 요약] ID로 분석 리포트를 조회
     *
     * 특정 ID에 해당하는 분석 리포트를 Optional 형태로 반환합니다.
     * 결과가 없을 경우 Optional.empty()를 반환하므로, 호출 측에서 존재 여부를 판단해야 합니다.
     *
     * @param id 조회할 분석 리포트의 ID
     * @return Optional<AnalysisReport>
     */
    public Optional<AnalysisReport> findReportById(Long id) {
        return repository.findById(id);
    }
}
