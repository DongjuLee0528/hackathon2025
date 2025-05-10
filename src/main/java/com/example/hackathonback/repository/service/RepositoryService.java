package com.example.hackathonback.repository.service;

import com.example.hackathonback.repository.dto.RepositoryDto;
import com.example.hackathonback.repository.entity.Repository;
import com.example.hackathonback.repository.repository.RepositoryJpaRepository;
import com.example.hackathonback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 저장소 등록을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final UserService userService;                         // 사용자 정보 조회 서비스
    private final RepositoryJpaRepository repositoryRepository;    // 저장소 JPA 레포지토리

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
        Repository repository = new Repository();
        repository.setName(repositoryDto.getName());
        repository.setUserId(userId);

        // 저장소를 데이터베이스에 저장
        repositoryRepository.save(repository);
    }
}
