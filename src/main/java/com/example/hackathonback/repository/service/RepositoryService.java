package com.example.hackathonback.repository.service;

import com.example.hackathonback.repository.dto.RepositoryDto;
import com.example.hackathonback.repository.entity.RepositoryEntity;
import com.example.hackathonback.repository.repository.RepositoryRepository;
import com.example.hackathonback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final UserService userService;
    private final RepositoryRepository repositoryRepository;

    // ✅ 저장소 등록
    public void registerRepository(String email, RepositoryDto repositoryDto) {
        // 이메일로 사용자 ID 조회
        Long userId = userService.findUserIdByEmail(email);

        // 저장소 엔티티 생성 및 저장
        RepositoryEntity repositoryEntity = new RepositoryEntity();
        repositoryEntity.setName(repositoryDto.getName());
        repositoryEntity.setUserId(userId);

        // 저장소를 데이터베이스에 저장
        repositoryRepository.save(repositoryEntity);
    }
}
