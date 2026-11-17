# Support Ticket Management System

Spec-driven exercise: **requirements → spec → plan → implementation → tests → review**.

## Workflow artefacts
| Path | Purpose |
|------|---------|
| `spec/` | Requirements, architecture, API, state machine, UI, tests |
| `rules/` | Java, testing, API standards for AI/human review |
| `commands/` | Reusable Cursor prompts (review-code, review-spec, generate-tests) |
| `docs/ai-review-log.md` | Corrected AI suggestions |
| `.specstory/history/` | Prompt session history |

## Prerequisites
- Java 21
- Maven 3.9+ (or use downloaded binary as in CI notes)
- Node 20+

## Run backend (H2 file DB — survives restart)
```bash
cd backend
mvn spring-boot:run
```
API: `http://localhost:8080/api/v1/tickets`

PostgreSQL profile:
```bash
export SPRING_PROFILES_ACTIVE=postgres
export DATABASE_URL=jdbc:postgresql://localhost:5432/tickets
export DATABASE_USERNAME=tickets
export DATABASE_PASSWORD=yourpassword
mvn spring-boot:run
```

## Run frontend
```bash
cd frontend
npm install
npm run dev
```
Open `http://localhost:3000` (Next.js rewrites `/api/*` to the backend).

## Test backend
```bash
cd backend && mvn test
```
State machine integration tests: `TicketStateMachineIntegrationTest`.

## State machine (enforced server-side)
`OPEN → IN_PROGRESS → RESOLVED → CLOSED`; `OPEN|IN_PROGRESS → CANCELLED`. Invalid transitions return **409 Conflict**.

## Acceptance checklist
See `spec/requirements.md` and `spec/test-strategy.md`.