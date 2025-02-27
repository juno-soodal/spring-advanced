package org.example.expert.domain.manager.service.component;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerWriter {
    private final ManagerRepository managerRepository;
    public void create(Manager newManagerUser) {
        managerRepository.save(newManagerUser);
    }

    public void delete(Manager manager) {
        managerRepository.delete(manager);
    }
}
