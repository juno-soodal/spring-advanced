package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.todo.service.component.TodoFinder;
import org.example.expert.domain.todo.service.component.TodoReader;
import org.example.expert.domain.todo.service.component.TodoWriter;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoWriter todoWriter;
    @Mock
    private TodoReader todoReader;
    @Mock
    private TodoFinder todoFinder;
    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private TodoService todoService;


    @Test
    public void 일정_저장_성공() {

        //given
        AuthUser authUser = new AuthUser(1L, "asd@asd.com", UserRole.USER);
        TodoSaveRequest request = new TodoSaveRequest("title", "asdasd");

        given(weatherClient.getTodayWeather()).willReturn("굿데이");

        User user = User.fromAuthUser(authUser);
        Todo todo = new Todo(request.getTitle(), request.getContents(), "맑음", user);

        doNothing().when(todoWriter).create(any(Todo.class));
        //when
        TodoSaveResponse todoSaveResponse = todoService.saveTodo(authUser, request);
        //then
        assertEquals(authUser.getId(),todoSaveResponse.getUser().getId());
        assertEquals(request.getContents(),todoSaveResponse.getContents());
        assertEquals("굿데이",todoSaveResponse.getWeather());

        verify(weatherClient, times(1)).getTodayWeather();
        verify(todoWriter, times(1)).create(any(Todo.class));

    }

    @Test
    public void 전체_일정_조회() {

        //given
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        User user = new User("asd@asd.com", "asdasd", UserRole.USER);
        ReflectionTestUtils.setField(user,"id", 1L);
        Todo todo1 = new Todo("title", "asdasd", "맑음", user);
        Todo todo2 = new Todo("title", "asdasd", "맑음", user);
        ReflectionTestUtils.setField(todo1,"id", 1L);
        ReflectionTestUtils.setField(todo2,"id", 2L);
        List<Todo> mockTodos = List.of(todo1,todo2);
        Page<Todo> mockPage = new PageImpl<>(mockTodos, pageable, mockTodos.size());
        given(todoReader.findTodosWithUserOrderByModifiedAtDesc(pageable)).willReturn(mockPage);

        //when
        Page<TodoResponse> responsePage = todoService.getTodos(page, size);
        List<TodoResponse> todos = responsePage.getContent();
        //then

        assertEquals(todos.size(),2);
        verify(todoReader, times(1)).findTodosWithUserOrderByModifiedAtDesc(pageable);
    }

    @Test
    public void todo를_todoId로_조회할_수_있다() {
            
        //given
        User user = new User("asd@asd.com", "asdasd", UserRole.USER);
        ReflectionTestUtils.setField(user,"id", 1L);
        Todo todo = new Todo("title", "asdasd", "맑음", user);
        long todoId = 1L;
        ReflectionTestUtils.setField(todo,"id", todoId);
        given(todoFinder.findWithUser(todoId)).willReturn(todo);
        //when
        TodoResponse todoResponse = todoService.getTodo(todoId);
        //then
        assertEquals(todo.getContents(),todoResponse.getContents());
    }

    @Test
    public void 없는_일정_조회_시_InvalidRequestException_을_던진다() {
        //given
        long todoId = 1L;
        given(todoFinder.findWithUser(todoId)).willThrow(new InvalidRequestException("Todo not found"));
        //when & Then
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> todoService.getTodo(todoId), "Todo not found");

        assertEquals("Todo not found", e.getMessage());

    }
}