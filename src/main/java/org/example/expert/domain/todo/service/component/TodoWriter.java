package org.example.expert.domain.todo.service.component;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoWriter {

    private final TodoRepository todoRepository;

    public void create(Todo todo) {
        todoRepository.save(todo);
    }
}
