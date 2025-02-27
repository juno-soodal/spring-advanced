package org.example.expert.config;

import io.jsonwebtoken.Claims;
import org.example.expert.domain.user.enums.UserRole;

public interface JwtUtil {

    String createToken(Long userId, String email, UserRole userRole);

    String substringToken(String tokenValue);

    Claims extractClaims(String token);
}
