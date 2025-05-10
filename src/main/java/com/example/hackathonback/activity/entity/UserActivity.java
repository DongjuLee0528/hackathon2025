
package com.example.hackathonback.activity.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String action; // 예: "SUBMIT", "GRADE_CHANGE", "LOGIN"
    private String details;
    private LocalDateTime timestamp = LocalDateTime.now();

    public UserActivity() {}
    
    public UserActivity(String userEmail, String action, String details) {
        this.userEmail = userEmail;
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
