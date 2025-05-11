package com.example.hackathonback.activity.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // [한 줄 요약] 이 클래스는 사용자 활동 내역을 저장하는 JPA 엔티티입니다.
// UserActivity 엔티티는 사용자의 다양한 활동 이력을 기록하여 DB에 저장합니다.
// 예를 들어 문제 제출, 로그인, 점수 변경 등의 행동을 추적할 수 있습니다.
public class UserActivity {

    @Id // [한 줄 요약] 기본 키 필드입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동 증가 방식으로 ID 생성
    private Long id;

    private String userEmail; // [한 줄 요약] 활동을 수행한 사용자의 이메일
    // 이 필드는 활동을 수행한 사용자를 식별하는 데 사용됩니다.

    private String action; // [한 줄 요약] 활동 종류 (예: SUBMIT, GRADE_CHANGE, LOGIN 등)
    // 사용자가 수행한 행동의 종류를 나타냅니다.
    // 예: SUBMIT(제출), GRADE_CHANGE(점수 변경), LOGIN(로그인) 등

    private String details; // [한 줄 요약] 활동에 대한 상세 설명
    // 활동에 대한 부가 정보를 담습니다.
    // 예: 제출한 문제 ID, 변경된 점수 내용 등

    private LocalDateTime timestamp = LocalDateTime.now(); // [한 줄 요약] 활동 발생 시각
    // 객체 생성 시 자동으로 현재 시간으로 설정됩니다.

    public UserActivity() {}
    // JPA에서 기본 생성자를 필요로 하기 때문에 정의되어 있음

    public UserActivity(String userEmail, String action, String details) {
        this.userEmail = userEmail;
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now(); // 활동 시각 초기화
    }
}
