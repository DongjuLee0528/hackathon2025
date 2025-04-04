package com.example.hackathonback.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RepositoryEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Long userId;
}
