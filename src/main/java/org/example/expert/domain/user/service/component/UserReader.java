package org.example.expert.domain.user.service.component;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public Optional<User> find(Long userId) {
        return userRepository.findById(userId);
    }

    public boolean exists(String email) {
        return userRepository.existsByEmail(email);
    }
}
