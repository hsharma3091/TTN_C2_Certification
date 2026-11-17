package com.example.tickets.web.dto;

import com.example.tickets.domain.Comment;

import java.time.Instant;

public record CommentResponse(Long id, Long ticketId, String body, String author, Instant createdAt) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTicket().getId(),
                comment.getBody(),
                comment.getAuthor(),
                comment.getCreatedAt()
        );
    }
}