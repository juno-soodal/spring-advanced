package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 관리자 권한이 없는 경우 403을 반환합니다.
        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));
        if (UserRole.ADMIN != userRole) {
            throw new AuthException("관리자 권한이 없습니다.");
        }
        return true;
    }
}
