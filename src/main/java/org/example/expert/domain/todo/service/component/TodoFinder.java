package org.example.expert.domain.todo.service.component;


import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoFinder {

    private final TodoRepository todoRepository;

    public Todo find(long todoId) {
        return todoRepository.findById(todoId).orElseThrow(() ->
                new InvalidRequestException("Todo not found"));
    }

    public Todo findWithUser(long todoId) {
        return todoRepository.findByIdWithUser(todoId).orElseThrow(() ->
                new InvalidRequestException("Todo not found"));
    }
}
