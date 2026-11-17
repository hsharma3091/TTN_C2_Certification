package com.example.tickets.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI supportTicketsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Support Tickets API")
                        .description("REST API for ticket lifecycle, search, comments, and status transitions.")
                        .version("1.0.0"));
    }
}