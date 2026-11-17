package com.example.tickets.web;

import com.example.tickets.domain.TicketStatus;
import com.example.tickets.service.TicketService;
import com.example.tickets.web.dto.CreateTicketRequest;
import com.example.tickets.web.dto.TicketResponse;
import com.example.tickets.web.dto.UpdateTicketRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "Tickets", description = "Create, list, search, filter, read, and update tickets (not status)")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @Operation(summary = "Create ticket", description = "New tickets start in status OPEN")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "400", description = "Validation error")
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody CreateTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.create(request));
    }

    @GetMapping
    @Operation(summary = "List tickets", description = "Optional query: status (exact), q (title/description search)")
    public List<TicketResponse> list(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) String q) {
        return ticketService.list(status, q);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by id")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    public TicketResponse get(@PathVariable Long id) {
        return ticketService.get(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update ticket fields", description = "Partial update; use transitions endpoint for status")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    public TicketResponse update(@PathVariable Long id, @Valid @RequestBody UpdateTicketRequest request) {
        return ticketService.update(id, request);
    }
}