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

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // github or gitlab
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        final String email;
        final String username;

        if ("github".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            username = oAuth2User.getAttribute("login");
        } else if ("gitlab".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            username = oAuth2User.getAttribute("username");
        } else {
            throw new IllegalArgumentException("지원하지 않는 OAuth 서비스입니다: " + registrationId);
        }

        if (email == null || username == null) {
            throw new IllegalArgumentException("OAuth 사용자 정보가 충분하지 않습니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .username(username)
                            .provider(registrationId)
                            .build();
                    return userRepository.save(newUser);
                });

        return new DefaultOAuth2User(
                Collections.emptyList(),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }
}