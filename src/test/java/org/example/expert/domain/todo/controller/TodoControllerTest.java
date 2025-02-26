package org.example.expert.domain.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.config.JwtFilter;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.JwtUtilImpl;
import org.example.expert.config.MockJwtUtil;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private JwtUtil jwtUtil;

    private static final String MOCK_TOKEN = "Bearer mock-token";


    @BeforeEach
    void beforeEach() {
        this.jwtUtil = new MockJwtUtil();
        mockMvc = MockMvcBuilders.standaloneSetup(new TodoController(todoService))
                .setCustomArgumentResolvers(new AuthUserArgumentResolver())
                .addFilter(new JwtFilter(jwtUtil), "/*")
                .build();
    }

    @Test
    public void 목록_조회_빈리스트() throws Exception {

        //given
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        given(todoService.getTodos(page, size)).willReturn(new PageImpl<>(List.of(), pageable, 0L));
        System.out.println(new PageImpl<>(List.of(), pageable, 0L));
        //when & then

        mockMvc.perform(get("/todos")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .header("Authorization", MOCK_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.number").value(1));

    }

    @Test
    public void 목록_조회() throws Exception {

        //given
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        UserResponse userResponse = new UserResponse(1L, "test123@.com");
        List<TodoResponse> todoResponses = List.of(
                new TodoResponse(1L, "제목1", "내용1", "맑음", userResponse, LocalDateTime.now(), LocalDateTime.now()),
                new TodoResponse(1L, "제목1", "내용1", "맑음", userResponse, LocalDateTime.now(), LocalDateTime.now())
        );
        given(todoService.getTodos(page, size)).willReturn(new PageImpl<>(todoResponses, pageable, todoResponses.size()));

        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/todos")
                        .param("page", String.valueOf(page)).param("size", String.valueOf(size))
                        .header("Authorization", MOCK_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value(todoResponses.get(0).getTitle()));

    }

    @Test
    public void Todo_단건_조회() throws Exception {

        //given
        long todoId = 1L;
        UserResponse userResponse = new UserResponse(1L, "test123@.com");
        TodoResponse todoResponse = new TodoResponse(1L, "제목1", "내용1", "맑음", userResponse, LocalDateTime.now(), LocalDateTime.now());
        given(todoService.getTodo(todoId)).willReturn(todoResponse);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/todos/{todoId}", todoId)
                        .header("Authorization", MOCK_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value(todoResponse.getTitle()))
                .andExpect(jsonPath("$.user.email").value(userResponse.getEmail()));

    }

    @Test
    public void Todo_단건_조회_토큰_없으면_SC_BAD_REQUEST() throws Exception {

        //given
        long todoId = 1L;
        UserResponse userResponse = new UserResponse(1L, "test123@.com");
        TodoResponse todoResponse = new TodoResponse(1L, "제목1", "내용1", "맑음", userResponse, LocalDateTime.now(), LocalDateTime.now());
        given(todoService.getTodo(todoId)).willReturn(todoResponse);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/todos/{todoId}", todoId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 일정_저장() throws Exception {
        //given
        TodoSaveRequest todoSaveRequest = new TodoSaveRequest();
        String title = "테스트 제목";
        ReflectionTestUtils.setField(todoSaveRequest, "title", title);
        String contents = "테스트 내용";
        ReflectionTestUtils.setField(todoSaveRequest, "contents", contents);

        long userId = 1L;
        String email = "asd@asd.com";
        UserResponse userResponse = new UserResponse(userId, email);
        TodoSaveResponse todoResponse = new TodoSaveResponse(userId, title, contents, "맑음", userResponse);
        given(todoService.saveTodo(any(AuthUser.class), any(TodoSaveRequest.class))).willReturn(todoResponse);
        //when


        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", MOCK_TOKEN)
                        .content(new ObjectMapper().writeValueAsString(todoSaveRequest)))
                .andExpect(status().isOk());
//
        //then

    }
}