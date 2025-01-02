package com.example.ecommerce_tut.service;

import com.example.ecommerce_tut.model.Comment;
import com.example.ecommerce_tut.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
