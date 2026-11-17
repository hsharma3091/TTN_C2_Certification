package com.example.tickets.service;

import com.example.tickets.domain.TicketStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(TicketStatus from, TicketStatus to) {
        super("Cannot transition from %s to %s".formatted(from, to));
    }
}