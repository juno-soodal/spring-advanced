package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.service.component.ManagerFinder;
import org.example.expert.domain.manager.service.component.ManagerReader;
import org.example.expert.domain.manager.service.component.ManagerWriter;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.service.component.TodoFinder;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.service.component.UserFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ManagerService {


    private final ManagerWriter managerWriter;
    private final ManagerReader managerReader;
    private final ManagerFinder managerFinder;
    private final TodoFinder todoFinder;
    private final UserFinder userFinder;

    @Transactional
    public ManagerSaveResponse saveManager(AuthUser authUser, long todoId, ManagerSaveRequest managerSaveRequest) {
        // 일정을 만든 유저
        User user = User.fromAuthUser(authUser);
        Todo todo = todoFinder.find(todoId);

        if (!isTodoOwner(todo, user)) {
            throw new InvalidRequestException("담당자를 등록하려고 하는 유저와 일정을 만든 유저가 유효하지 않습니다.");

        }

        User managerUser = userFinder.findManagerUser(managerSaveRequest.getManagerUserId());

        validateNotSelfAssign(user, managerUser);

        Manager newManagerUser = new Manager(managerUser, todo);
        managerWriter.create(newManagerUser);

        return new ManagerSaveResponse(
                newManagerUser.getId(),
                new UserResponse(managerUser.getId(), managerUser.getEmail())
        );
    }

    @Transactional(readOnly = true)
    public List<ManagerResponse> getManagers(long todoId) {

        Todo todo = todoFinder.find(todoId);

        List<Manager> managerList = managerReader.findWithUserByTodoId(todo.getId());

        List<ManagerResponse> dtoList = new ArrayList<>();
        for (Manager manager : managerList) {
            User user = manager.getUser();
            dtoList.add(new ManagerResponse(
                    manager.getId(),
                    new UserResponse(user.getId(), user.getEmail())
            ));
        }
        return dtoList;
    }

    @Transactional
    public void deleteManager(long userId, long todoId, long managerId) {

        User user = userFinder.findById(userId);

        Todo todo = todoFinder.find(todoId);


        if (!isTodoOwner(todo, user)) {
            throw new InvalidRequestException("해당 일정을 만든 유저가 유효하지 않습니다.");
        }

        Manager manager = managerFinder.find(managerId);

        if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
            throw new InvalidRequestException("해당 일정에 등록된 담당자가 아닙니다.");
        }

        managerWriter.delete(manager);
    }

    private void validateNotSelfAssign(User user, User managerUser) {
        if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
            throw new InvalidRequestException("일정 작성자는 본인을 담당자로 등록할 수 없습니다.");
        }
    }

    private static boolean isTodoOwner(Todo todo, User user) {
        return todo.getUser() != null && ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId());
    }
}
