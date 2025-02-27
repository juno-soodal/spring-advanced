package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.service.component.UserFinder;
import org.example.expert.domain.user.service.component.UserReader;
import org.example.expert.domain.user.service.component.UserWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserReader userReader;
    private final UserWriter userWriter;
    private final UserFinder userFinder;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {

        if (userReader.exists(signupRequest.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        User user = new User(
                signupRequest.getEmail(),
                encodedPassword,
                userRole
        );

        userWriter.create(user);

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), userRole);

        return new SignupResponse(bearerToken);
    }

    @Transactional(readOnly = true)
    public SigninResponse signin(SigninRequest signinRequest) {
        User user = userFinder.findByEmail(signinRequest.getEmail());

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SigninResponse(bearerToken);
    }
}
