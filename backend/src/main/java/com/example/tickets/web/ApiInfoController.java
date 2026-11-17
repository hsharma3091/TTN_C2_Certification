package com.example.tickets.web;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Hidden
public class ApiInfoController {

    @GetMapping
    public Map<String, Object> apiInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name", "Support Tickets API");
        info.put("version", "v1");
        info.put("tickets", "/api/v1/tickets");
        info.put("openapi", "/api-docs");
        info.put("swaggerUi", "/swagger-ui/index.html");
        return info;
    }
}