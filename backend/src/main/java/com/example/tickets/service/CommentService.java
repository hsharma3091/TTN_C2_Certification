package com.example.tickets.service;

import com.example.tickets.domain.Comment;
import com.example.tickets.domain.Ticket;
import com.example.tickets.repository.CommentRepository;
import com.example.tickets.repository.TicketRepository;
import com.example.tickets.web.dto.CommentResponse;
import com.example.tickets.web.dto.CreateCommentRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public CommentResponse add(Long ticketId, CreateCommentRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setBody(request.body().trim());
        comment.setAuthor(request.authorOrDefault());
        commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> list(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new TicketNotFoundException(ticketId);
        }
        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId).stream()
                .map(CommentResponse::from)
                .toList();
    }
}