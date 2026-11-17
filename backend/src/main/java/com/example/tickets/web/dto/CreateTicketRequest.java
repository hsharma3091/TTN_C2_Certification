package com.example.tickets.web.dto;

import com.example.tickets.domain.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank @Size(max = 200) String title,
        @Size(max = 10_000) String description,
        TicketPriority priority,
        @Size(max = 120) String assignee
) {
    public TicketPriority priorityOrDefault() {
        return priority != null ? priority : TicketPriority.MEDIUM;
    }
}