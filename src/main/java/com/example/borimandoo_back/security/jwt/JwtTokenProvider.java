package com.example.borimandoo_back.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 액세스 토큰 발급
    public String createAccessToken(UUID userId, long expirationMillis) {
        return generateToken(userId, expirationMillis, "액세스");
    }

    // 리프레시 토큰 발급
    public String createRefreshToken(UUID userId, long expirationMillis) {
        return generateToken(userId, expirationMillis, "리프레시");
    }

    // 공통 토큰 생성 로직
    private String generateToken(UUID userId, long expirationMillis, String type) {
        String token = Jwts.builder()
                .claim("userId", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey())
                .compact();

        log.info("발급된 {} 토큰: {}", type, token);
        return token;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 토큰입니다: {}", e.getMessage());
            return false;
        }
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            boolean expired = expiration.before(new Date());
            log.info("토큰 만료 여부: {}", expired);
            return expired;

        } catch (JwtException | IllegalArgumentException e) {
            log.warn("토큰 만료 확인 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    // 토큰에서 userId를 username처럼 반환
    public UUID getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String userIdString = claims.get("userId", String.class);
            return UUID.fromString(userIdString);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("getUserIdFromToken 중 토큰 검증 실패: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    // 토큰에서 역할(Role) 추출
    public String getRole(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("role", String.class);
        } catch (JwtException e) {
            log.error("getRole 중 토큰 검증 실패: {}", e.getMessage());
            throw new RuntimeException();
        }
    }

}
