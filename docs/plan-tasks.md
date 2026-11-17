# Plan / Tasks

## Phase 1 — Spec & steering ✅
- [x] `rules/*`, `commands/*`, `skills/documentation`
- [x] `spec/*` seven artefacts
- [x] Prompt history + AI review log

## Phase 2 — Backend ✅
- [x] JPA entities, repositories
- [x] `StatusTransitionPolicy` + dedicated transition endpoint
- [x] Validation + ProblemDetail errors
- [x] H2 file persistence + postgres profile

## Phase 3 — Frontend ✅
- [x] List, search, filter, create
- [x] Detail: edit fields, transitions, comments
- [x] Error banner from API

## Phase 4 — Test & review ✅
- [x] Parameterized state machine unit tests
- [x] MockMvc integration tests
- [ ] Optional: frontend component tests

## Phase 5 — Fix loop
- Track issues in `docs/ai-review-log.md` when validating Copilot/Cursor output.