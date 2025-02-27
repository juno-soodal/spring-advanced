package org.example.expert.domain.todo.service.component;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoReader {

    private final TodoRepository todoRepository;

    public Page<Todo> findTodosWithUserOrderByModifiedAtDesc(Pageable pageable) {
        return todoRepository.findAllByOrderByModifiedAtDesc(pageable);
    }
}
