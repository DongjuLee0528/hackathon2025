package com.example.hackathonback.repository.service;

import com.example.hackathonback.repository.dto.RepositoryDto;
import com.example.hackathonback.repository.entity.RepositoryEntity;
import com.example.hackathonback.repository.repository.RepositoryRepository;
import com.example.hackathonback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 저장소 등록을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final UserService userService;                   // 사용자 정보 조회 서비스
    private final RepositoryRepository repositoryRepository; // 저장소 저장소 (JPA)

    /**
     * 사용자 이메일을 기반으로 저장소 등록
     *
     * @param email 로그인된 사용자 이메일
     * @param repositoryDto 저장소 정보 (name 포함)
     */
    public void registerRepository(String email, RepositoryDto repositoryDto) {
        // 이메일로 사용자 ID 조회
        Long userId = userService.findUserIdByEmail(email);

        // 저장소 엔티티 생성
        RepositoryEntity repositoryEntity = new RepositoryEntity();
        repositoryEntity.setName(repositoryDto.getName());
        repositoryEntity.setUserId(userId);

        // 저장소를 데이터베이스에 저장
        repositoryRepository.save(repositoryEntity);
    }
}
