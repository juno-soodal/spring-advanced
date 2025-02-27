package org.example.expert.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWriter {

    private final UserRepository userRepository;

    public void create(User user) {
        userRepository.save(user);
    }
}
