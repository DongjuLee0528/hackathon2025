
package com.example.hackathonback.user.service;

import com.example.hackathonback.user.entity.User;
import com.example.hackathonback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        String email = (String) oAuth2User.getAttributes().get("email");
        String username = (String) oAuth2User.getAttributes().get("name");

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("OAuth 계정에 이메일이 존재하지 않습니다.");
        }

        userRepository.findByEmail(email)
                .map(user -> {
                    user.updateUsername(username);
                    return userRepository.save(user);
                })
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .username(username)
                                .provider(registrationId)
                                .build()
                ));

        log.info("OAuth 로그인 성공 - Provider: {}, Email: {}, Name: {}", registrationId, email, username);

        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_USER"),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }
}
