package org.example.expert.domain.todo.service.component;

import org.example.expert.domain.common.exception.InvalidRequestException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoFinderTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoFinder todoFinder;



    @Test
    public void 존재하지_않는_todoId로_조회하면_InvalidRequestException_발생한다() {
        //given
        Long todoId = 1L;
        given(todoRepository.findById(any(Long.class))).willReturn(Optional.empty());
        //when & then
        InvalidRequestException invalidRequestException = assertThrows(InvalidRequestException.class, () -> todoFinder.find(todoId));
        assertEquals("Todo not found", invalidRequestException.getMessage());
    }

    @Test
    public void todoId로_조회하면_일정을유저와함께_가져올_수_있다() {
        //given
        Long todoId = 1L;
        User user = new User("asd@asd.com", "asdasd", UserRole.USER);
        ReflectionTestUtils.setField(user,"id", 1L);
        Todo todo = new Todo("제목","내용","맑음",user);
        ReflectionTestUtils.setField(todo,"id", todoId);
        given(todoRepository.findByIdWithUser(any(Long.class))).willReturn(Optional.of(todo));
        //when
        Todo findTodo = todoFinder.findWithUser(todoId);

        assertEquals(todoId,findTodo.getId());
        assertEquals(user.getEmail(),todo.getUser().getEmail());

    }

    @Test
    public void 존재하지_않는_todoId로_일정과유정보를_조회하면_InvalidRequestException() {
        //given
        Long todoId = 1L;
        given(todoRepository.findByIdWithUser(any(Long.class))).willReturn(Optional.empty());
        //when
        InvalidRequestException invalidRequestException = assertThrows(InvalidRequestException.class, () -> todoFinder.findWithUser(todoId));
        assertEquals("Todo not found", invalidRequestException.getMessage());

    }
}