package org.example.expert.domain.manager.service.component;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerReader {

    private final ManagerRepository managerRepository;

    public List<Manager> findWithUserByTodoId(Long todoId) {
        return managerRepository.findByTodoIdWithUser(todoId);
    }
}
