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
API base: `http://localhost:8080/api/v1`

| Use case | Where |
|----------|--------|
| **Swagger UI** (try all endpoints) | http://localhost:8080/swagger-ui/index.html |
| **OpenAPI JSON** | http://localhost:8080/api-docs |
| **IntelliJ HTTP Client** | `backend/http/tickets-api.http` |
| **Contract** | `spec/api-contract.md`, `rules/api-standards.md` |

## Test API with curl

Start the backend first, then:

```bash
BASE=http://localhost:8080
ID=1   # replace with id from create response
```

**API info**

```bash
curl -s "$BASE/api/v1"
```

**Create ticket**

```bash
curl -s -X POST "$BASE/api/v1/tickets" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Login broken","description":"Cannot reset password","priority":"HIGH","assignee":"alice@example.com"}'
```

**List all**

```bash
curl -s "$BASE/api/v1/tickets"
```

**List — filter by status**

```bash
curl -s "$BASE/api/v1/tickets?status=OPEN"
```

**List — search**

```bash
curl -s "$BASE/api/v1/tickets?q=login"
```

**Get by id**

```bash
curl -s "$BASE/api/v1/tickets/$ID"
```

**Update (partial)**

```bash
curl -s -X PATCH "$BASE/api/v1/tickets/$ID" \
  -H 'Content-Type: application/json' \
  -d '{"priority":"URGENT","assignee":"bob@example.com"}'
```

**Transitions** (happy path: run in order)

```bash
curl -s -X POST "$BASE/api/v1/tickets/$ID/transitions" \
  -H 'Content-Type: application/json' \
  -d '{"status":"IN_PROGRESS"}'

curl -s -X POST "$BASE/api/v1/tickets/$ID/transitions" \
  -H 'Content-Type: application/json' \
  -d '{"status":"RESOLVED"}'

curl -s -X POST "$BASE/api/v1/tickets/$ID/transitions" \
  -H 'Content-Type: application/json' \
  -d '{"status":"CLOSED"}'
```

**Cancel** (from `OPEN` or `IN_PROGRESS` only)

```bash
curl -s -X POST "$BASE/api/v1/tickets/$ID/transitions" \
  -H 'Content-Type: application/json' \
  -d '{"status":"CANCELLED"}'
```

**Invalid transition** (expect HTTP 409)

```bash
curl -s -w "\nHTTP %{http_code}\n" -X POST "$BASE/api/v1/tickets/$ID/transitions" \
  -H 'Content-Type: application/json' \
  -d '{"status":"OPEN"}'
```

**List comments**

```bash
curl -s "$BASE/api/v1/tickets/$ID/comments"
```

**Add comment**

```bash
curl -s -X POST "$BASE/api/v1/tickets/$ID/comments" \
  -H 'Content-Type: application/json' \
  -d '{"body":"Investigating","author":"alice"}'
```

**OpenAPI JSON**

```bash
curl -s "$BASE/api-docs"
```

PostgreSQL profile:
```bash
export SPRING_PROFILES_ACTIVE=postgres
export DATABASE_URL=jdbc:postgresql://localhost:5432/tickets
export DATABASE_USERNAME=tickets
export DATABASE_PASSWORD=yourpassword
mvn spring-boot:run
```

## IntelliJ IDEA

1. **Open** the repo root folder (`support-ticket-system-intellij`) — not only `backend/`.
2. **JDK 21**: *File → Project Structure → SDKs* → add Java 21  
   (e.g. `/home/himanshu/.local/jdk/jdk-21.0.5+11` or `sudo apt install openjdk-21-jdk`).
3. **Maven**: trust/load the root `pom.xml`; wait for the `tickets` module import.
4. **Node.js** (for UI): *Settings → Languages & Frameworks → Node.js* → set interpreter to Node 20+  
   (e.g. after `nvm use 20`: `~/.nvm/versions/node/v20.20.2/bin/node`).
5. **Run** (toolbar dropdown):
   - `TicketsApplication` — Spring Boot on port **8080**
   - `frontend (npm dev)` — Next.js on port **3000**
   - `Full stack` — starts both

Configs live in `.run/`. If Spring Boot fails with “module not specified”, edit the run config and pick module **`tickets`**.

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