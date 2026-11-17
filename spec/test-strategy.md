# Test Strategy

## P0 — State machine (must pass)
- Unit: all allowed edges succeed; matrix of forbidden edges fail
- Integration: `POST /transitions` returns 409 with detail for invalid

## P1 — CRUD + search
- Create ticket defaults OPEN
- PATCH updates fields without changing status
- GET list filter by status and q

## P2 — Validation
- Blank title → 400
- Comment empty body → 400

## P3 — Persistence
- Integration test restart not required; rely on JPA flush + new transaction read

## Frontend
- Manual script in README; optional component tests later