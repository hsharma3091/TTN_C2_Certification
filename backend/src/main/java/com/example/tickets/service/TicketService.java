package com.example.tickets.service;

import com.example.tickets.domain.StatusTransitionPolicy;
import com.example.tickets.domain.Ticket;
import com.example.tickets.domain.TicketStatus;
import com.example.tickets.repository.TicketRepository;
import com.example.tickets.web.dto.CreateTicketRequest;
import com.example.tickets.web.dto.TicketResponse;
import com.example.tickets.web.dto.UpdateTicketRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public TicketResponse create(CreateTicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.title().trim());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priorityOrDefault());
        ticket.setAssignee(blankToNull(request.assignee()));
        ticket.setStatus(TicketStatus.OPEN);
        ticketRepository.save(ticket);
        return toResponse(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> list(TicketStatus status, String q) {
        String query = q != null ? q.trim() : null;
        return ticketRepository.search(status, query).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse get(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public TicketResponse update(Long id, UpdateTicketRequest request) {
        Ticket ticket = find(id);
        if (request.title() != null) {
            if (request.title().isBlank()) {
                throw new IllegalArgumentException("title must not be blank");
            }
            ticket.setTitle(request.title().trim());
        }
        if (request.description() != null) {
            ticket.setDescription(request.description());
        }
        if (request.priority() != null) {
            ticket.setPriority(request.priority());
        }
        if (request.assignee() != null) {
            ticket.setAssignee(blankToNull(request.assignee()));
        }
        return toResponse(ticket);
    }

    @Transactional
    public TicketResponse transition(Long id, TicketStatus target) {
        Ticket ticket = find(id);
        TicketStatus current = ticket.getStatus();
        if (!StatusTransitionPolicy.canTransition(current, target)) {
            throw new InvalidStatusTransitionException(current, target);
        }
        ticket.setStatus(target);
        return toResponse(ticket);
    }

    private Ticket find(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
    }

    private TicketResponse toResponse(Ticket ticket) {
        return TicketResponse.from(ticket, StatusTransitionPolicy.allowedTargets(ticket.getStatus()));
    }

    private static String blankToNull(String value) {
        return value != null && value.isBlank() ? null : value;
    }
}