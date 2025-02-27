package org.example.expert.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.comment.service.component.CommentWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentAdminService {

    private final CommentWriter commentWriter;

    @Transactional
    public void deleteComment(long commentId) {
        commentWriter.deleteById(commentId);

    }
}
