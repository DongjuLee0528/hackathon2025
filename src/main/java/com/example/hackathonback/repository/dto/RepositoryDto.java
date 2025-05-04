package com.example.hackathonback.repository.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 저장소 등록 요청 시 사용되는 DTO
 */
@Getter
@Setter
public class RepositoryDto {

    private String name; // 저장소 이름 (예: hackathon-backend)
}
