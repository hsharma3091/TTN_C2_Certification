# Architecture

```
┌─────────────┐     HTTP/JSON      ┌──────────────────┐
│  Next.js    │ ◄───────────────► │  Spring Boot     │
│  (React)    │   /api/v1/*       │  REST + JPA      │
└─────────────┘                   └────────┬─────────┘
                                         │
                                         ▼
                                ┌──────────────────┐
                                │ H2 / PostgreSQL  │
                                └──────────────────┘
```

## Backend modules (package `com.example.tickets`)
- `TicketStatus` enum + `StatusTransitionPolicy` (pure Java)
- `Ticket`, `Comment` JPA entities
- `TicketService`, `CommentService`
- `TicketController`, `CommentController`
- `GlobalExceptionHandler` → ProblemDetail JSON

## Frontend
- Next.js App Router, client components for forms/lists
- `lib/api.ts` — fetch wrapper, error parsing
- Pages: list (search/filter), detail (edit, transition, comments)

## Cross-cutting
- CORS: `http://localhost:3000`
- Flyway/Liquibase: optional; use `ddl-auto=update` for exercise H2 only (document prod migration need)