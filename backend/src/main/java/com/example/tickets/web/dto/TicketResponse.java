package com.example.tickets.web.dto;

import com.example.tickets.domain.Ticket;
import com.example.tickets.domain.TicketPriority;
import com.example.tickets.domain.TicketStatus;

import java.time.Instant;
import java.util.Set;

public record TicketResponse(
        Long id,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        String assignee,
        Instant createdAt,
        Instant updatedAt,
        Set<TicketStatus> allowedTransitions
) {
    public static TicketResponse from(Ticket ticket, Set<TicketStatus> allowed) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getAssignee(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                allowed
        );
    }
}