package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
public class MockJwtUtil implements JwtUtil{

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String MOCK_TOKEN ="mock-token";

    public String createToken(Long userId, String email, UserRole userRole) {
        return BEARER_PREFIX + MOCK_TOKEN;
    }

    public String substringToken(String tokenValue) {
        return MOCK_TOKEN;
    }

    public Claims extractClaims(String token) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(1L));

        claims.put("email","test@asd.com");
        claims.put("userRole", UserRole.USER.name());
        return claims;
    }
}
