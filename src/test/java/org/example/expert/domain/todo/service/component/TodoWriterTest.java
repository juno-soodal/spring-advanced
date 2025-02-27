package org.example.expert.domain.todo.service.component;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TodoWriterTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoWriter todoWriter;

    @Test
    public void todo를_저장할_수_있다() {
        //given
        User user = new User("asd@asdm.com", "1q2w3e4r", UserRole.USER);
        ReflectionTestUtils.setField(user,"id",1L);
        Todo todo = new Todo("wpahr","contents","맑음",user);
        //when
        todoWriter.create(todo);
        //then

        verify(todoRepository,times(1)).save(any(Todo.class));
    }
}