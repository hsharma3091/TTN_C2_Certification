# Testing Guidelines

## Backend (JUnit 5 + Spring Boot Test)
- **Unit**: state machine transitions, validation logic (no Spring context)
- **Integration**: `@SpringBootTest` + Testcontainers or H2 for repositories and REST (`@AutoConfigureMockMvc`)
- **State machine**: parameterized tests for every allowed transition and representative invalid transitions from `spec/state-machine.md`

## Frontend
- Manual acceptance against core criteria; optional React Testing Library for critical forms
- E2E out of scope unless time permits

## Naming
- `*Test` unit; `*IT` or `*IntegrationTest` for full context
- Method names: `shouldReject_whenClosedToOpen`

## Data
- Use `@Sql` or `TestEntityManager` for isolated fixtures
- `@DirtiesContext` only when necessary

## Commands
- Backend: `./mvnw test` from `backend/`
- Frontend: `npm test` from `frontend/` when configured

## AI-generated tests
- Reject tests that mock away the transition under test
- Reject tests that assert wrong HTTP codes vs contract