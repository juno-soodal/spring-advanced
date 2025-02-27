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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoReaderTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoReader todoReader;

    @Test
    public void todo_목록을_조회_할_수_있다() {
        //given
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page-1,size);
        User user = new User("asd@asd.com", "asdasd", UserRole.USER);
        ReflectionTestUtils.setField(user,"id", 1L);
        Todo todo1 = new Todo("title", "asdasd", "맑음", user);
        Todo todo2 = new Todo("title", "asdasd", "맑음", user);
        ReflectionTestUtils.setField(todo1,"id", 1L);
        ReflectionTestUtils.setField(todo2,"id", 2L);
        List<Todo> mockTodos = List.of(todo1,todo2);
        Page<Todo> todos = new PageImpl<>(mockTodos, pageable, mockTodos.size());
        given(todoRepository.findAllByOrderByModifiedAtDesc(pageable)).willReturn(todos);
        //when
        Page<Todo> responsePage = todoReader.findTodosWithUserOrderByModifiedAtDesc(pageable);
        //then
        assertEquals(2,responsePage.getContent().size());
    }
}