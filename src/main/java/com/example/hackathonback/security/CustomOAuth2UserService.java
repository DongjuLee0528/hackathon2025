package com.example.hackathonback.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * GitHub OAuth2 사용자 정보를 프로젝트 도메인 유저와 매핑/자동가입을 할 때 사용.
 * 지금은 DB 연동 없이 기본 정보만 매핑하고 ROLE_USER 권한을 부여합니다.
 *
 * 나중에 DB를 붙일 때:
 *  - attributes 에서 id/login/email/name 받아서 회원 조회/생성
 *  - 내부 userId 를 subject 로 쓰고 추가 권한 부여
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 원본 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 프로바이더별 유니크 키 (application.properties 의 user-name-attribute 값과 일치)
        String nameAttributeKey = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // github: login (혹은 id)

        // TODO: 여기서 DB 조회/자동가입 로직 추가 가능
        // ex) Long githubId = ((Number) attributes.get("id")).longValue();
        //     Optional<User> user = userRepository.findByGithubId(githubId);
        //     ...

        // 기본 권한 부여
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // 그대로 전달 (JWT 생성은 성공 핸들러에서 처리)
        return new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
    }
}
