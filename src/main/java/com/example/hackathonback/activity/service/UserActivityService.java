package com.example.hackathonback.activity.service;

import com.example.hackathonback.activity.entity.UserActivity;
import com.example.hackathonback.activity.repository.UserActivityRepository;
import org.springframework.stereotype.Service;

@Service // [한 줄 요약] 사용자 활동 기록 로직을 처리하는 서비스 클래스입니다.
// 이 클래스는 사용자의 활동을 로그로 남기기 위한 비즈니스 로직을 담당합니다.
// 주로 컨트롤러나 다른 서비스에서 호출되어 활동 기록을 DB에 저장합니다.
public class UserActivityService {

    private final UserActivityRepository repository; // [한 줄 요약] 활동 기록 저장을 위한 JPA 리포지토리 의존성

    // UserActivityService 생성자: 리포지토리를 주입받아 초기화합니다.
    public UserActivityService(UserActivityRepository repository) {
        this.repository = repository;
    }

    // [한 줄 요약] 사용자 활동을 기록하는 메서드
    // 전달받은 이메일, 활동 종류, 상세 내용을 기반으로 UserActivity 객체를 생성하고 저장합니다.
    public void logActivity(String userEmail, String action, String details) {
        repository.save(new UserActivity(userEmail, action, details));
    }
}
