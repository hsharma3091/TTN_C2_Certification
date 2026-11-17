package com.example.tickets.web;

import com.example.tickets.service.TicketService;
import com.example.tickets.web.dto.TicketResponse;
import com.example.tickets.web.dto.TransitionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets/{ticketId}/transitions")
@Tag(name = "Ticket transitions", description = "State machine status changes (409 on illegal transition)")
public class TicketTransitionController {

    private final TicketService ticketService;

    public TicketTransitionController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @Operation(
            summary = "Change ticket status",
            description = "Allowed paths: OPEN→IN_PROGRESS|CANCELLED; IN_PROGRESS→RESOLVED|CANCELLED; RESOLVED→CLOSED"
    )
    @ApiResponse(responseCode = "200", description = "Status updated")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    @ApiResponse(responseCode = "409", description = "Invalid status transition")
    public TicketResponse transition(
            @PathVariable Long ticketId,
            @Valid @RequestBody TransitionRequest request) {
        return ticketService.transition(ticketId, request.status());
    }
}