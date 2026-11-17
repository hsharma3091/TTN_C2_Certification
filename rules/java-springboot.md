# Java Spring Boot Guidelines

## Stack
- Java 21 (records, pattern matching where appropriate)
- Spring Boot 3.3+
- Spring Data JPA
- Jakarta Validation (`jakarta.validation`)
- PostgreSQL in production profile; H2 for local/test

## Layering
- `domain` — entities, enums, state-machine rules (no framework imports in pure domain services where possible)
- `repository` — Spring Data interfaces
- `service` — business logic, transactions, status transitions
- `web` — REST controllers, DTOs, `GlobalExceptionHandler`

## Conventions
- REST resources under `/api/v1/`
- Use DTOs for request/response; never expose entities directly
- IDs: `Long` surrogate keys; UUID optional for external refs
- Timestamps: `Instant` or `OffsetDateTime` stored UTC
- `@Transactional` on service layer only
- Constructor injection; avoid field injection

## State machine
- Status changes only via dedicated service method (`transitionStatus`)
- Reject illegal transitions with `409 Conflict` or `422 Unprocessable Entity` and clear message
- Do not allow PATCH to set `status` without transition rules

## Security (exercise scope)
- No secrets in repo; use env vars / Spring `application-*.yml` with placeholders
- CORS configured explicitly for frontend origin

## AI review checklist
- Verify enum transitions match `spec/state-machine.md`
- Ensure validation annotations match `spec/api-contract.md`
- No `System.out` / swallowed exceptions