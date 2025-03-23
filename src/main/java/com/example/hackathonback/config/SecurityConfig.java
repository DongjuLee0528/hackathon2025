import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/git/**").authenticated() // 이제 인증 필요
                        .anyRequest().permitAll()
                )
                .oauth2Login(Customizer.withDefaults()) // OAuth 로그인 활성화
                .csrf(csrf -> csrf.disable())
                .build();
    }
}
