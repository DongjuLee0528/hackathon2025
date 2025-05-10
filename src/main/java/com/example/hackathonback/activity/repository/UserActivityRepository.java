
package com.example.hackathonback.activity.repository;

import com.example.hackathonback.activity.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
}
