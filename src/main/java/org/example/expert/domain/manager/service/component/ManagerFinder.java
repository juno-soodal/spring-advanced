package org.example.expert.domain.manager.service.component;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerFinder {

    private final ManagerRepository managerRepository;


    public Manager find(long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new InvalidRequestException("Manager not found"));
    }
}
