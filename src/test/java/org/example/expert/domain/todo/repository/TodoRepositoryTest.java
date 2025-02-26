package org.example.expert.domain.todo.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void 일정에_유저_정보를_포함해서_가져온다() {
            //given
        User user = new User("test@a.com","1q2w3e4r", UserRole.USER);
        userRepository.save(user);
        Todo todo1 = new Todo("제목1","내용","test",user);
        todoRepository.save(todo1);
        Pageable pageable = PageRequest.of(0, 10);

        PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();

        //when
        Page<Todo> result = todoRepository.findAllByOrderByModifiedAtDesc(pageable);
        List<Todo> todos = result.getContent();
        //then
        Assertions.assertTrue(persistenceUnitUtil.isLoaded(todos.get(0).getUser()));

    }
}