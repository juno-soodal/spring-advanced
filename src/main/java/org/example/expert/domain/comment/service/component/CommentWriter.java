package org.example.expert.domain.comment.service.component;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentWriter {

    private final CommentRepository commentRepository;

    public void create(Comment newComment) {
        commentRepository.save(newComment);
    }

    public void deleteById(long commentId) {
        commentRepository.deleteById(commentId);
    }
}
