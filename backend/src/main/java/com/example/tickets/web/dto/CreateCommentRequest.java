package com.example.tickets.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(
        @NotBlank @Size(max = 10_000) String body,
        @Size(max = 120) String author
) {
    public String authorOrDefault() {
        return author != null && !author.isBlank() ? author : "system";
    }
}