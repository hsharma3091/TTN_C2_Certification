# AI Review Log — Rejected or Corrected Suggestions

Engineering assistant posture: document mistakes rather than blind acceptance.

| # | AI suggestion | Problem | Resolution |
|---|---------------|---------|------------|
| 1 | Expose `status` on PATCH for convenience | Bypasses state machine | Status only via `POST /transitions` |
| 2 | Use `@ElementCollection` for comments | Over-normalized wrong model | Separate `Comment` entity + FK |
| 3 | Return `200` on illegal transition with `success: false` | Hides client errors | `409 Conflict` + ProblemDetail |
| 4 | Client-side only state checks | Backend must enforce | Duplicate policy in Java + tests |
| 5 | `ddl-auto=create-drop` for demo | Data lost on restart | `update` + file-based H2 |
| 6 | Single enum `nextStatuses()` without tests | Drift risk | Parameterized JUnit matrix |

Add new rows when reviewing Copilot/Cursor output during implementation.