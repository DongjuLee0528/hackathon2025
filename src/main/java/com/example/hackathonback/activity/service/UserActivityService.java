
package com.example.hackathonback.activity.service;

import com.example.hackathonback.activity.entity.UserActivity;
import com.example.hackathonback.activity.repository.UserActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class UserActivityService {

    private final UserActivityRepository repository;

    public UserActivityService(UserActivityRepository repository) {
        this.repository = repository;
    }

    public void logActivity(String userEmail, String action, String details) {
        repository.save(new UserActivity(userEmail, action, details));
    }
}
