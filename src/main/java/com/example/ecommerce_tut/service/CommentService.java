package com.example.ecommerce_tut.service;

import com.example.ecommerce_tut.dto.CommentDTO;
import com.example.ecommerce_tut.exception.ResourceNotFoundException;
import com.example.ecommerce_tut.mapper.CommentMapper;
import com.example.ecommerce_tut.model.Comment;
import com.example.ecommerce_tut.model.Product;
import com.example.ecommerce_tut.model.User;
import com.example.ecommerce_tut.repository.CommentRepository;
import com.example.ecommerce_tut.repository.ProductRepository;
import com.example.ecommerce_tut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CommentDTO addComment(Long productId, Long userId, CommentDTO commentDto) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setUser(user);
        comment.setProduct(product);

        Comment savedComment = commentRepository.save(comment);
        return this.commentMapper.toDTO(savedComment);
    }
    public List<CommentDTO> getCommentsByProductId(Long productId) {
        List<Comment> comments = this.commentRepository.findByProductId(productId);
        return comments.stream().map(this.commentMapper::toDTO).toList();
    }
}
