package com.example.hackathonback.activity.repository;

import com.example.hackathonback.activity.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

// [한 줄 요약] UserActivity 엔티티를 위한 JPA Repository 인터페이스입니다.
// 이 인터페이스는 사용자 활동(UserActivity)에 대한 기본적인 CRUD 기능을 제공합니다.
// JpaRepository를 상속하면 save, findById, findAll, delete 등 여러 메서드를 별도 구현 없이 사용할 수 있습니다.
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    // 현재는 기본 CRUD만 사용하지만, 필요 시 커스텀 쿼리 메서드도 정의 가능ㅇㅇ
}
