package org.example.expert.domain.comment.service.component;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentReader {

    private final CommentRepository commentRepository;


    public List<Comment> findWithUser(long todoId) {
        return commentRepository.findByTodoIdWithUser(todoId);
    }
}
