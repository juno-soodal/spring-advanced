package org.example.expert.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFinder {

    private final UserRepository userRepository;

    public User findManagerUser(Long managerUserId) {

        return userRepository.findById(managerUserId).orElseThrow(() -> new InvalidRequestException("등록하려고 하는 담당자 유저가 존재하지 않습니다."));
    }

    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new InvalidRequestException("가입되지 않은 유저입니다."));
    }
}
