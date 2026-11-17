package com.example.tickets.web;

import com.example.tickets.domain.TicketStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketStateMachineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private long ticketId;

    @BeforeEach
    void createTicket() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Integration ticket","description":"test","priority":"HIGH"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        ticketId = json.get("id").asLong();
    }

    @Test
    void shouldFollowHappyPath_toClosed() throws Exception {
        transition(ticketId, TicketStatus.IN_PROGRESS);
        transition(ticketId, TicketStatus.RESOLVED);
        transition(ticketId, TicketStatus.CLOSED);

        mockMvc.perform(post("/api/v1/tickets/{id}/transitions", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("CLOSED")));
    }

    @Test
    void shouldReject_closedToOpen() throws Exception {
        transition(ticketId, TicketStatus.IN_PROGRESS);
        transition(ticketId, TicketStatus.RESOLVED);
        transition(ticketId, TicketStatus.CLOSED);

        mockMvc.perform(post("/api/v1/tickets/{id}/transitions", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReject_resolvedToOpen() throws Exception {
        transition(ticketId, TicketStatus.IN_PROGRESS);
        transition(ticketId, TicketStatus.RESOLVED);

        mockMvc.perform(post("/api/v1/tickets/{id}/transitions", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReject_cancelledToOpen() throws Exception {
        transition(ticketId, TicketStatus.CANCELLED);

        mockMvc.perform(post("/api/v1/tickets/{id}/transitions", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void patchShouldNotChangeStatus() throws Exception {
        mockMvc.perform(patch("/api/v1/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated title\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

  private void transition(long id, TicketStatus status) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/tickets/{id}/transitions", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"" + status + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("status").asText()).isEqualTo(status.name());
    }
}