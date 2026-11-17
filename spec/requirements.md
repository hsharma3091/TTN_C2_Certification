# Requirements — Support Ticket Management System

## Functional
1. **Create ticket** — title (required), description (optional), priority (LOW|MEDIUM|HIGH|URGENT), default status OPEN, optional assignee.
2. **List tickets** — show id, title, status, priority, assignee, updated time.
3. **View details** — full ticket + comment thread.
4. **Update** — title, description, priority, assignee (not status via this endpoint).
5. **Comments** — add text; list chronologically.
6. **Search** — keyword `q` matches title or description (case-insensitive).
7. **Filter** — by `status` enum.
8. **Persistence** — relational DB; data survives restart.

## Non-functional
- Backend validation; UI shows API error messages.
- REST JSON API for SPA.
- Java 21, Spring Boot, PostgreSQL (prod) / H2 (dev).

## State machine (backend-enforced)
- OPEN → IN_PROGRESS | CANCELLED
- IN_PROGRESS → RESOLVED | CANCELLED
- RESOLVED → CLOSED
- Terminal: CLOSED, CANCELLED
- All other status changes rejected.

## Out of scope
- Auth/RBAC, attachments, email notifications, SLA timers.