# Documentation Skill

When documenting this project:

1. Read `spec/requirements.md` and `spec/api-contract.md` before writing user-facing docs.
2. Keep `README.md` as entry point: prerequisites, run backend, run frontend, profiles (H2 vs Postgres).
3. OpenAPI: maintain `backend/src/main/resources/openapi.yaml` in sync with controllers.
4. Record spec changes in `docs/changelog.md` with date and rationale.
5. Do not duplicate API tables — link to `spec/api-contract.md`.