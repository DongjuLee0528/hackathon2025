package com.example.hackathonback.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidityInMs;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidityInMs;

    private Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /** ✅ Access Token 생성 */
    public String createAccessToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityInMs);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** ✅ Refresh Token 생성 */
    public String createRefreshToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidityInMs);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** ✅ 토큰 유효성 검사 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** ✅ 토큰에서 사용자 ID(이메일) 추출 */
    public String getUserEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // createAccessToken()에서 setSubject(userId) 로 저장됨
    }

    /** ✅ Access Token 유효시간 반환 */
    public long getAccessTokenValidity() {
        return accessTokenValidityInMs;
    }

    /** ✅ Refresh Token 유효시간 반환 */
    public long getRefreshTokenValidity() {
        return refreshTokenValidityInMs;
    }
}
